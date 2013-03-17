
(function($)
{
	var Func = Backbone.Model.extend({
		//urlRoot: "api/func",
		defaults: {
			"id": null,
			"name": null,
			"desc": "Sample Description",
			"image":"Sample Image"
		}
	});

	var FuncCollection = Backbone.Collection.extend({
		model: Func,
		//url: "api/func"
	});


	var FuncListView = Backbone.View.extend({

		el: $("#func-list"),
		template: _.template($('#func-list-item').html()),
		initialize: function(options) {
			this.render();
		},

		render: function() {
			$el=this.el;
			$template=this.template;
			_.each(this.model.models, function (m) {
				$($el).append($template(m.toJSON()));
			});
		},

		
		close: function() {
			$(this.el).unbind();
			$(this.el).remove();
		}
	});

	var FuncView = Backbone.View.extend({

		el: $('#func-desc'),

		template: _.template($('#func-info').html()),

		initialize: function() {
			this.render();
		},

		render: function() {
			$(this.el).html(this.template(this.model.toJSON()));
		},

		
		close: function() {
			$(this.el).unbind();
			$(this.el).empty();
		}
	});


	var AppRouter = Backbone.Router.extend({

		routes: {
			""					: "list",
			"funcDetail/:id"	: "funcDetails"
		},

		list: function() {name
			this.funclist = new FuncCollection([new Func({"id":1, "name":"Time Table Generator"}),
			                 new Func({"id":2, "name":"Report Card Management"}),
			                 new Func({"id":3, "name":"Student Profile Management"})]);
			this.funcListView = new FuncListView({model: this.funclist});
			//this.funcList.fetch();
		},

		funcDetails: function(id) {
			this.func = this.funclist.get(id);
			if (app.funcView) app.funcView.close();
			this.funcView = new FuncView({model: this.func});
//			this.funcView.render();
		}

	});

	var app = new AppRouter();
	Backbone.history.start();

})(jQuery);




