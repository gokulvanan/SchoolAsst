
var Profile = function(opts)
{
	
	var prof = $.extend( {
		'dropDownOpts'	: [],
		'studentsList'	: []
	}, opts);

	var SearchView = Backbone.View.extend({
		el: $("#searchDiv"),
		
		initialize: function(){
			this.data=prof.dropDownOpts;
			this.template =_.template( $("#search_template").html(), {opts:this.data} );
			this.render();
		},
		render: function()
		{
			this.$el.html(this.template);
			$("select.classDropdown").val(prof.selectedClass);
			this.initializeAutoComplete();
		},
		events: {
			"change .classDropdown"          : "initializeAutoComplete",
		
		},
		
		initializeAutoComplete: function(){
			console.log("initializeAutoComplete");
			var clazz=$("select.classDropdown").val();
			var students=prof.studentsList[clazz];

			$( "#studentName" ).autocomplete({
				minLength: 0,
				source: students,
				focus: function( event, ui ) {
					$( "#studentName" ).val( ui.item.label );
					return false;
				},
				select: function( event, ui ) {
					$( "#studentName" ).val( ui.item.label );
					$( "#studentId" ).val( ui.item.value );
					// Fire search
					console.log("FireSearch")
				}
			});


		},
		
		changeGridHeader: function() {
			console.log("changeGridHeader");
			var clazz =  $('select.classDropdown').val();
			rep.selectedSubj=parseInt($("select."+clazz+"_subj").val());
			if(rep.grid){
				resetGrid();	
			}else{
				rep.grid = initializeGrid();
			}
			
		}
	});
	
	return{
		init : function(){
			var srch = new SearchView();
		}
	}
}