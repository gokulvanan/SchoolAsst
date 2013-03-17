
function TimeTable(opts)
{
	var t = $.extend( {
		'dropDownOpts'	: []
	}, opts);
	
	var TimeTableModel = Backbone.Model.extend({
		urlRoot:"/TimeTableManager/fetchTimeTable/",
		initialize: function(obj){
			this.id=obj.id;
			this.type=obj.type;
		},

		url: function() {
		      var base = _.result(this, 'urlRoot') || _.result(this.collection, 'url') || urlError();
		      if (this.isNew()) return base;
		      return base + (base.charAt(base.length - 1) === '/' ? '' : '/') + encodeURIComponent(this.id)+"/"+encodeURIComponent(this.type);
		    },

	});

	var TimeTableView = Backbone.View.extend({
		el: $("#timeTableDiv"),
		initialize: function(){
			var caller = this.options.callType;
			var mod = this.model.attributes;
			console.log("mod here");
			console.log(mod);
			if(!mod.timeTable)
			{	
				console.log("here");
				$("h1.nodatamsg").show();
				$("div#timeTableDiv").hide();
			}
			else
			{
				$("h1.nodatamsg").hide();
				$("div#timeTableDiv").show();
				t.header=mod.timeTable[0];
				if(t.grid)
				{
					resetGrid(mod.timeTable);
				}
				else
				{
					t.grid=initializeGrid();
				}
				loadData(mod.timeTable,caller);
			}
			
		},
		render: function()
		{
			this.$el.html(this.template);
		},
	});

	function loadData(mod,caller)
	{
		var gridData = [];
		_.each(mod, function(dayMap){
			_.each(dayMap, function(val,key){
				var row ={id:key, days:key};
				_.each(val, function (period){
					if(!period.subj){
						row[period.periodId]="Break";
					}
					else{
						if(caller === 'teacher'){
							row[period.periodId]='<br/><span class="clazz"><a href="#selectOpt/'+period.clazzId+'/class">'+period.clazz+'</a></span> <br/> <br/>'+
							'<span class="subj">Subject: &nbsp;'+period.subj+'</span> <br/>';
						}else{
							row[period.periodId]='<br/>	<span class="tchr"><a href="#selectOpt/'+period.tchrId+'/teacher">'+period.tchr+'</a></span> <br/> <br/>'+
							'<span class="subj"> Subject: &nbsp;'+period.subj+'</span> <br/>';
						}
					}
				});
				gridData.push(row);
			});
		});
		t.grid.setGridParam({data:gridData});
		t.grid.trigger("reloadGrid");
	}
	
	function resetGrid()
	{
		t.grid.clearGridData();
		var colData=computeColHeaders();
		t.grid.setGridParam({colNames:colData.colnms,	colModel:colData.colType});
		t.grid.destroyGroupHeader();	
		t.grid.setGroupHeaders({useColSpanStyle: true, groupHeaders:colData.grpHdrs});
	}
	
	function computeColHeaders()
	{
		var colnms=["Days"];
		var colType=[{name:'days',index:'days', width:20, sortable:false}];
		var grpHdrs=[];
		_.each(t.header, function(val,key){
			_.each(val, function(header){
				colnms.push(header.name);
				colType.push({name:header.periodId, index:header.periodId, width:30, sortable:false, title:false,align:"center"});
				grpHdrs.push({startColumnName: header.periodId, numberOfColumns: 1, titleText:header.durationLabel});
			});
		});
		
		
		return{
			"colnms" :colnms,
			"colType":colType,
			"grpHdrs":grpHdrs
		};
	}

	function initializeGrid(){
		
		console.log("buildJgridCall");
		var colData=computeColHeaders();
		return jQuery("#timetable").jqGrid({
			datatype: "local",
			data:[],
			width:1600,
			height: 410,
			colNames:colData.colnms,
			colModel:colData.colType,
//			sortorder: "asc",
//			sortname: "days",
			//altRows:true,
			pager:"timetable_pagn",
			rowNum:7,
			rowList: [],        // disable page size dropdown
			pgbuttons: false,     // disable page control like next, back button
			pgtext: null,         // disable pager text like 'Page 0 of 10'
			viewrecords: false,    // disable current view record text like 'View 1-10 of 100'
			forceFit : true,
			cellEdit: true
			
			}).setGroupHeaders({
			  useColSpanStyle: true, 
			  groupHeaders:colData.grpHdrs
			});
	}
	var SearchView = Backbone.View.extend({
		el: $("#searchDiv"),
		initialize: function(){
			this.data= eval(this.options.data);
			this.template =_.template( $("#search_template").html(), {teachers:this.data.teachers, classes:this.data.classes} );
		},
		render: function()
		{
			this.$el.html(this.template);
		},
		events: {
			    "change .classDropdown"           : "fetchClassTimeTable",
			    "change .teacherDropdown"         : "fetchTeacherTimeTable"
		},
		fetchClassTimeTable: function() {
			$(".teacherDropdown").val('0');
			var id=$(".classDropdown").val();
			var type = "class";
			router.navigate("fetchTimeTable/"+id+"/"+type, {trigger: true});
		},
		fetchTeacherTimeTable: function(elm) {
			$(".classDropdown").val('0');
			var id=$(".teacherDropdown").val();
			var type = "teacher";
			router.navigate("fetchTimeTable/"+id+"/"+type, {trigger: true});
		}
	});


	var Router = Backbone.Router.extend({
		routes:{
			'fetchTimeTable/:id/:type' : 'fetchTimeTable',
			'selectOpt/:id/:type'	   : 'selectOpt'	
		},
		fetchTimeTable: function(id,type){
			console.log("call");
			var clazz = new TimeTableModel({"id":id, "type":type});
			clazz.fetch({
		        success: function () {
		            var timetable = new TimeTableView({"model":clazz, callType:type});
		            timetable.render();
		        }
		    });
		},
		selectOpt : function(id,type)
		{
			if(type === 'teacher'){
				$(".teacherDropdown").val(id);
				$(".classDropdown").val('0');
				router.navigate("fetchTimeTable/"+id+"/"+type, {trigger: true});
			}
			else
			{
				$(".classDropdown").val(id);
				$(".teacherDropdown").val('0');
				router.navigate("fetchTimeTable/"+id+"/"+type, {trigger: true});
			}
			
		}
		
	});

	var router = new Router();
	Backbone.history.start();
	return {
		init : function(){
			var search_view = new SearchView({data:t.dropDownOpts});
			search_view.render();
		}
	}
}







