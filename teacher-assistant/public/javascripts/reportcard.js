var Report = function(opts)
{
	var rep = $.extend( {
		'dropDownOpts'	: [],
		'selectedClass'	: -1,
		'selectedSubj'  : -1,
		'header' 		: [],
		'students' 		: [],
		'studentMarks'  : [],
		'initLoad'		: true
	}, opts);

	var SearchView = Backbone.View.extend({
		el: $("#searchDiv"),
		
		initialize: function(){
			this.data=rep.dropDownOpts;
			rep.header=this.data.subjTestTemplateMap;// all subjects testTemplate map for columnHeaders
			this.template =_.template( $("#search_template").html(), {opts:this.data} );
			this.render();
			rep.initLoad=false;
		},
		render: function()
		{
			this.$el.html(this.template);
			$("select.classDropdown").val(rep.selectedClass);
			var clazz=$("select.classDropdown").val();
			var subj=this.getSubjectDropDown(clazz);
			this.changeGridHeader(clazz,subj);
		},
		events: {
			"change .classDropdown"          : "fetchClassList",
			"change .subjDropDown"           : "fetchSubjMarksList",
		},
		
		fetchClassList: function(){
			console.log("fetchClassList");
			var clazz=$("select.classDropdown").val();
			var subj=this.getSubjectDropDown(clazz);
			if(!rep.initLoad){
				router.navigate("fetchClasslist/"+clazz, {trigger: true});
			}
			this.changeGridHeader(clazz,subj);
		},
		
		fetchSubjMarksList: function(){
			console.log("fetchSubjMarks");
			var clazz=$("select.classDropdown").val();
			var subj=$("select."+clazz+"_subj").val();
			if(!rep.initLoad){
				router.navigate("fetchSubjMarksList/"+clazz+"/"+subj, {trigger: true});
			}
			this.changeGridHeader(clazz,subj);
		},
		
		getSubjectDropDown: function(clazz){
			$("select.subjDropDown").hide();// hide all
			$("select."+clazz+"_subj").show();
			if(rep.selectedSubj !== -1){
				$("select."+clazz+"_subj").val(rep.selectedSubj);
				rep.selectedSub=-1;
			}
			return $("select."+clazz+"_subj").val();
		},
		
		changeGridHeader: function(clazz,subj) {
			console.log("changeGridHeader");
			var subjMap=rep.dropDownOpts.classSubjMap[clazz+""];
			var subjName=subjMap[subj];
			if(rep.grid){
				resetGrid(subj, subjName);	
			}else{
				rep.grid = initializeGrid(subj,subjName);
			}
			
		}
	});

	var Student=Backbone.Model.extend();


	var Students = Backbone.Collection.extend({
		model:Student,

		initialize: function(obj){
			this.url=obj.url
		}

	});

	var StudentListView = Backbone.View.extend({
		initialize: function(obj){
			console.log(obj);
			this.students=obj.students;
			this.studentMarks=obj.studentMarks;
			this.render();
		},
		render: function()
		{
			var gridData=[];
			console.log("render")
			if(this.students){
				if(!this.students["null"])
				{
					gridData=this.renderStudents(gridData,this.students);
					rep.gridData=gridData;
				}
			}
			else{
				gridData=rep.gridData;
			}
			if(this.studentMarks && !this.studentMarks["null"]){
				gridData=this.renderStudentsMarks(gridData,this.studentMarks);
			}
			console.log(gridData);
			rep.grid.setGridParam({"data":gridData}).trigger("reloadGrid");
			console.log("Done");
		},
		
		renderStudents: function(gridData,students){
			_.each(students, function(student){
				var data={id:student.id, rollNo:student.rollNo, name:student.name};
				gridData.push(data);
			});
			return gridData;
		},
		
		renderStudentsMarks:function(gridData,studentMarks){
			var i=0;
			_.each(studentMarks, function(testMarkMap, id){
				var data= gridData[i++];
				_.each(testMarkMap, function(val,id){
					data[id]=val.marks;
				});
			});
			return gridData;
		}

	});

	var Router = Backbone.Router.extend({
		routes:{
			''									: "loadClasses",	
			'fetchClasslist/:id' 				: 'fetchClasslist',
			'fetchSubjMarksList/:id/:subjId' 	: 'fetchSubjMarksList'
		},
		
		fetchClasslist: function(id){
			console.log("fetchClasslist");
			var url="/ReportCardManager/fetchStudents/"+id;
			var students = new Students({"url":url});
			students.fetch({
				success: function () {
					var models = students.models;
				//	var data={"students":eval(models[0].attributes),"studentMarks":eval(models[1].attributes)};
					console.log(models);
					rep.grid.clearGridData();
				//	new StudentListView(data);
				}
			});
		},
		
		fetchSubjMarksList: function(id, subjId){
			console.log("fetchSubjMarksList");
			var url="/ReportCardManager/fetchSubjMarksList/"+id+"/"+subjId;
			var students = new Students({"url":url});
			students.fetch({
				success: function () {
					var models = students.models;
					console.log(models)
//					var data={"studentMarks":eval(models[0].attributes)};
					rep.grid.clearGridData();
				//	new StudentListView(data);
				}
			});
		}

	});

	var router = new Router();
	Backbone.history.start()
	
	function resetGrid(subjId,subjName)
	{
		var colData=computeColHeaders(subjId,subjName);
		rep.grid.setGridParam({colNames:colData.colnms,	colModel:colData.colType});
		console.log(colData.colType);
		rep.grid.destroyGroupHeader();	
		rep.grid.setGroupHeaders({useColSpanStyle: true, groupHeaders:colData.grpHdrs});
		rep.grid.trigger("reloadGrid");
	}
	
	function computeColHeaders(subjId,subjName)
	{
		var colnms=["Roll No", "Name"];
		var colType=[{name:'rollNo',index:'rollNo', width:35, sorttype:"int"},
		             {name:'name',index:'name', width:60, sorttype:"string", formatter:'showlink', formatoptions:{baseLinkUrl:window.location.origin+"/StudentProfiler/fetchStudent"}}];
		var grpHdrs=[];
		var head = rep.header;
		var testList=head[subjId+""];
		var i=0;
		var startCol='';
		console.log(testList);
		_.each(testList, function(test){
			startCol= (i++ === 0) ? test.id : startCol;
			colnms.push(test.name);
			colType.push({name:test.id, index:test.id, width:30, sorttype:"float",editable:true, title:false});
		});
		grpHdrs.push({startColumnName: startCol, numberOfColumns:testList.length, titleText:subjName});

		return{
			"colnms" :colnms,
			"colType":colType,
			"grpHdrs":grpHdrs
		};
	}
	
	function initializeGrid(subjId,subjName){
		
		console.log("buildJgridCall");
		var colData=computeColHeaders(subjId,subjName);
		
		rep.grid = jQuery("#students_list").jqGrid({
			datatype: "local",
			width:1600,
			height: 580,
			colNames:colData.colnms,
			colModel:colData.colType,
			sortorder: "asc",
			sortname: "rollNo",
			//altRows:true,
			pager:"students_list_pagination",
			rowNum:100,
			rowList: [],        // disable page size dropdown
			pgbuttons: false,     // disable page control like next, back button
			pgtext: null,         // disable pager text like 'Page 0 of 10'
			viewrecords: false,    // disable current view record text like 'View 1-10 of 100'
			forceFit : true,
			cellEdit: true,
			cellsubmit: 'clientArray',
			/*loadComplete:function(){
				var rows =[];//$(this).datagrid("getRows");
				console.log(rows);
				if(rows.length === 0)
				{
				  $(".datagrid-view").append('<div class="errordiv" id="errordiv">No data Founds</div>');
				  $("#errordiv").show();
				}
				else
				{
					$("#errordiv").hide();
				}
			},*/
			//RULES OF validation for the two methods below should be configurable
			// Need to show max marks as well
			// need to add my promp snipped in here
			afterEditCell: function (id,name,val,iRow,iCol){
				console.log("here")
				
			},
			afterSaveCell : function(rowid,name,val,iRow,iCol) {
				console.log("here after save");
			}
			}).navGrid("#students_list_pagination",{edit:false,add:false,del:false,refresh:false}).jqGrid('setGroupHeaders', {
			  useColSpanStyle: true, 
			  groupHeaders:colData.grpHdrs
			});
		return rep.grid;
	}

	return	{
		init: function(){
			console.log("here");
			new SearchView();
			new StudentListView({"students":rep.students,"studentMarks":rep.studentMarks});
		}
	};
}

