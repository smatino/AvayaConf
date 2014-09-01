
$(function(){
   var Event = Backbone.Model.extend({
       idAttribute:"title"
   });

    var Events = Backbone.Collection.extend({
        model: Event,
        url: 'events'
    });

    var EventsView = Backbone.View.extend({
        initialize: function(){
            _.bindAll(this);

            this.collection.bind('reset', this.addAll);
            this.collection.bind('add', this.addOne);
            this.collection.bind('change', this.change);
            this.collection.bind('destroy', this.destroy);

            this.eventView = new EventView();
        },
        render: function() {
            this.el.fullCalendar({
                header: {
                    left: 'prev,next today',
                    center: 'title',
                    right: 'month,basicWeek,basicDay'
                },
                selectable: true,
                selectHelper: true,
                editable: true,
                ignoreTimezone: false,
                select: this.select,
                eventClick: this.eventClick,
                eventDrop: this.eventDropOrResize,
                eventResize: this.eventDropOrResize
            });
        },
        addAll: function() {
            this.el.fullCalendar('addEventSource', this.collection.toJSON());
        },
        addOne: function(event) {
            this.el.fullCalendar('renderEvent', event.toJSON());
        },
        select: function(startDate, endDate) {
            this.eventView.collection = this.collection;
            this.eventView.model = new Event({start: startDate, end: endDate});
            this.eventView.render();
        },
        eventClick: function(fcEvent) {
            this.eventView.model = this.collection.get(fcEvent.title);
            this.eventView.render();
        },
        change: function(event) {
            // Look up the underlying event in the calendar and update its details from the model
            var fcEvent = this.el.fullCalendar('clientEvents', event.get('id'))[0];
            fcEvent.title = event.get('title');
            //fcEvent.color = event.get('color');
            this.el.fullCalendar('updateEvent', fcEvent);
        },
        eventDropOrResize: function(fcEvent) {
            // Lookup the model that has the ID of the event and update its attributes
            this.collection.get(fcEvent.title).save({start: fcEvent.start, end: fcEvent.end});
        },
        destroy: function(event) {
            this.el.fullCalendar('removeEvents', event.id);
        }
    });

    var EventView = Backbone.View.extend({
        el: $('#eventDialog'),
        initialize: function() {
            _.bindAll(this);
        },
        render: function() {
            var buttons = {'Ok': this.save};
            if (!this.model.isNew()) {
                _.extend(buttons, {'Delete': this.destroy});
            }
            _.extend(buttons, {'Cancel': this.close});

            this.el.dialog({
                modal: true,
                title: (this.model.isNew() ? 'New' : 'Edit') + ' Event',
                buttons: buttons,
                open: this.open
            });

            return this;
        },
        open: function() {
            this.$('#title').val(this.model.get('title'));
            this.$('#confRoom').val(this.model.get('confRoom'));
            this.$('#pin').val(this.model.get('pin'));
            this.$('#start').val(this.model.get('start'));
            this.$('#end').val(this.model.get('end'));
            this.$('#email').val(this.model.get('email'));
            this.$('#oraInizio').val(this.model.get('oraInizio'));
            this.$('#oraFine').val(this.model.get('oraFine'));
        },
        save: function() {
            if (this.model.isNew()) {
                this.model.set({'title': this.$('#title').val(), 'email': this.$('#email').val(), 'oraInizio' : this.$('#oraInizio').val(), 'oraFine' : this.$('#oraFine').val(), 'pin' : this.$('#pin').val(), 'confRoom' : this.$('#confRoom').val() });
                this.collection.create(this.model, {success: this.close});
            } else {
                this.model.set({'title': this.$('#title').val(), 'email': this.$('#email').val(), 'oraInizio' : this.$('#oraInizio').val(), 'oraFine' : this.$('#oraFine').val(), 'pin' : this.$('#pin').val(), 'confRoom' : this.$('#confRoom').val() });
                this.model.save({}, {success: this.close});
            }
        },
        close: function() {
            this.el.dialog('close');
        },
        destroy: function() {
            this.model.destroy({success: this.close});
        }
    });

    var events = new Events();
    new EventsView({el: $("#calendar"), collection: events}).render();
    events.fetch();
});