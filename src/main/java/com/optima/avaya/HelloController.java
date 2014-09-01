package com.optima.avaya;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/")
public class HelloController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    MailSender mailSender;

    @RequestMapping(value = "home",method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {
		model.addAttribute("message", "Hello world!");
		return "hello";
	}


    @RequestMapping(value = "/events",method = RequestMethod.GET)
    @ResponseBody
    public List<CalendarBean> getEvents(ModelMap model) {

      List<CalendarBean> result =  jdbcTemplate.query("select *  FROM [OPTIMA].[dbo].[CONF_OPTION]", new RowMapper<CalendarBean>() {
            @Override
            public CalendarBean mapRow(ResultSet rs, int rowNum) throws SQLException {
                CalendarBean bean = new CalendarBean();
                bean.setConfRoom(rs.getString("ID"));
                bean.setPin(rs.getString("PIN"));
                bean.setEmail(rs.getString("OWNER"));
                bean.setStart(rs.getDate("DATA1"));
                bean.setEnd(rs.getDate("DATA2"));
                bean.setTitle(rs.getString("TITLE"));
                bean.setOraInizio(rs.getString("ORA_INIZIO"));
                bean.setOraFine(rs.getString("ORA_FINE"));
                return bean;
            }
        });
        return result;
    }


    @RequestMapping(value = "/events/{eventTitle}",method = RequestMethod.DELETE)
    public @ResponseBody String deleteEvent(@PathVariable("eventTitle") String eventTitle) {

        int result =  jdbcTemplate.update("DELETE FROM [OPTIMA].[dbo].[CONF_OPTION] where  [OPTIMA].[dbo].[CONF_OPTION].TITLE = ?", eventTitle);
        return "{\"result\":\"OK\"}";
    }



    @RequestMapping(value = "/events",method = RequestMethod.PUT)
    public @ResponseBody String saveEvent(@RequestBody String data) throws IOException {
           return updateEvent("",data);
    }

    @RequestMapping(value = "/events/{eventTitle}",method = RequestMethod.PUT)
    public @ResponseBody String updateEvent(@PathVariable("eventTitle") String eventTitle, @RequestBody String data) throws IOException {


        ObjectMapper mapper = new ObjectMapper();

        CalendarBean bean = mapper.readValue(data, CalendarBean.class);


        bean.setTitle(eventTitle +"- Conf Room prenotata da "+bean.getEmail()+" - "+System.currentTimeMillis()+"\n");


        Calendar calStart = Calendar.getInstance();
        calStart.setTime(bean.getStart());

        if(bean.getOraInizio() != null && (bean.getOraInizio().contains(",") || bean.getOraInizio().contains(".") || bean.getOraInizio().contains(":"))){
            bean.setOraInizio(bean.getOraInizio().replaceAll("[\\.\\:\\,]",":"));
            String[] oraInizio = bean.getOraInizio().split(":");

            calStart.add(Calendar.HOUR, Integer.parseInt(oraInizio[0]));
            calStart.add(Calendar.MINUTE, Integer.parseInt(oraInizio[1]));
        } else{
            if(bean.getOraInizio() != null)  {
                calStart.add(Calendar.HOUR, Integer.parseInt(bean.getOraInizio()));
            }

        }



        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(bean.getEnd()!= null ? bean.getEnd() :  bean.getStart());

        if(bean.getOraFine() != null && (bean.getOraFine().contains(",") || bean.getOraFine().contains(".") ||bean.getOraFine().contains(":"))){
            bean.setOraFine(bean.getOraFine().replaceAll("[\\.\\:\\,]",":"));
            String[] oraFine = bean.getOraFine().split(":");

            calEnd.add(Calendar.HOUR, Integer.parseInt(oraFine[0]));
            calEnd.add(Calendar.MINUTE, Integer.parseInt(oraFine[1]));
        } else{
            if(bean.getOraFine() != null){
                calEnd.add(Calendar.HOUR, Integer.parseInt(bean.getOraFine()));
            }

        }

        int result  = jdbcTemplate.update("UPDATE [CONF_OPTION] set ID = ?, PIN = ? , DATA1= ? , DATA2 = ? , OWNER = ? , ORA_INIZIO= ? , ORA_FINE = ? WHERE TITLE = ?",bean.getConfRoom(),bean.getPin(), calStart.getTime(),calEnd.getTime(), bean.getEmail(),bean.getOraInizio(), bean.getOraFine(),eventTitle);

        if(result == 0){
            jdbcTemplate.update("INSERT INTO [CONF_OPTION](id,pin,data1,data2,owner, title, ora_inizio, ora_fine) VALUES(?,?,?,?,?,?,?,?)",bean.getConfRoom(),bean.getPin(), calStart.getTime(),calEnd.getTime(), bean.getEmail(), bean.getTitle(),bean.getOraInizio(), bean.getOraFine());

            SimpleMailMessage message = new SimpleMailMessage();

            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

            message.setFrom("confreservation@optimaitalia.com");
            message.setTo(bean.getEmail());
            message.setSubject("Conferma registrazione Conf Room "+bean.getConfRoom() );
            message.setText("Prenotazione room conference \n dalle "+bean.getOraInizio()+"  \n alle "+bean.getOraFine()+ " \n del "+df.format(calStart.getTime())+" \n Sala "+ bean.getConfRoom()+" \n PIN  "+bean.getPin()+" seguito dal tasto # \n Numero accesso interno *910* \n Numeri accesso esterni 08119630091 - 0299970001");
            mailSender.send(message);
        } else{
            SimpleMailMessage message = new SimpleMailMessage();

            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            message.setFrom("confreservation@optimaitalia.com");
            message.setTo(bean.getEmail());
            message.setSubject("Modifica registrazione Conf Room "+bean.getConfRoom() );
            message.setText("Modifica prenotazione room conference \n dalle "+bean.getOraInizio()+"  \n alle "+bean.getOraFine()+ " \n del "+df.format(calStart.getTime())+" \n Sala "+ bean.getConfRoom()+" \n PIN  "+bean.getPin()+" seguito dal tasto # \n Numero accesso interno *910* \n Numeri accesso esterni 08119630091 - 0299970001");
            mailSender.send(message);
        }


        return "{\"result\":\"OK\"}";
    }
}