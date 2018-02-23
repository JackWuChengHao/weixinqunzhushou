/**
 * @version 1.2
 * @author sun_zeming
 * @class
 * 用于显示数据表格的JavaScript控件. 集成的分页控件, 可对表格中的数据集进行客户端分页.
 *
 * @param {String} id: HTML节点的id, 控件将显示在该节点中.
 * @returns {TableView}: 返回分页控件实例.
 * @requires jQuery {@link PagerView} {@link SortView}
 *
 * @example
 * ### HTML代码:
 * &lt;div id="my_div"&gt;&lt;/div&gt;
 *
 * ### JavaScript代码:
 * var table = new TableView('my_div');
 * // id:列id(必)
 * // width:列宽度,单位%(必)
 * // align:对齐方式left、center、right(选)
 * // type:单选框radio、多选框checkbox(选)
 * // key:当前字段标识为主键(有且只有一个字段添加该属性)
 * // order:当前字段是否需要排序功能(选)
 * testTable.initHeader([
 * 		{id:'marker',width:5,align:'right',type:'radio'},//marker指单选多选栏
 * 		{id:'id',name:'序号',width:10,align:'center',key:true,order:true},
 * 		{id:'name',name:'姓名',width:85,align:'left'}
 * 						]);
 *
 * table.add({id:1, name:'Tom'});
 * table.render();
 * 
 * 修改某一行的背景颜色：给该行添加addClass属性，值域有active、success、warning、danger、info
 * table.add({id:1, name:'Tom',addClass:'danger'});	//该行背景为红色
 * 
 */
var TableView = function(id){

	/**
	 * 创建分页控件
	 */
	this.createPage = function(){
		$('#' + id+'-page').remove();
		var content = '<div id="'+id+'-page" style="text-align: center;"'+
		' pagination="pagination_new" pagenumber="1" totalpage="1"'+
		' paginationMaxLength="7"></div>';
		$("#" + id).after(content);
	};
	
	/* 因为哈希表的实现可能是元素无序的, 所以使用数组代替. 为此, 定义了数据操作方法. */
	function array_index_of_key(arr, key, val){
		for(var i in arr){
			if(arr[i][key] == val){
				return parseInt(i);
			}
		}
		return -1;
	}

	function array_index_of_item(arr, item){
		for(var i in arr){
			if(arr[i] == item){
				return parseInt(i);
			}
		}
		return -1;
	}

	function array_get(arr, key, val){
		var index = array_index_of_key(arr, key, val);
		if(index != -1){
			return arr[index];
		}
		return false;
	}

	function array_del(arr, key, val){
		var index = array_index_of_key(arr, key, val);
		if(index != -1){
			var a1 = arr.slice(0, index);
			var a2 = arr.slice(index + 1);
			return a1.concat(a2);
		}
		return arr;
	}

	var self = this;
	this.id = id;
	this._rendered = false;
	this._filter_text = '';
	this.rows = [];
	this._display_rows = []; // 过滤后的数据集
	
	/**
	 * 当前控件所处的HTML节点引用.
	 * @type DOMElement
	 */
	this.container = null;

	/**
	 * 数据集的每一条记录的唯一标识字段名. 类似数据库表的主键字段名.
	 * @type String
	 */
	this.dataKey = '';
	/**
	 * 要显示的数据表格的标题.
	 * @type String
	 */
	this.title = '';
	/**
	 * 要显示的记录的字段, 以及所对应的字段名. 如 'id' : '编号'.
	 * @type Object
	 */
	this.header = {};
	/**
	 * 需要排序的表头
	 */
	this.orderHeader = [];
	/**
	 * 表头宽度
	 */
	this.headerWidth = {};

//	this.pageisze = [10,20,30,40,50];
	this.pageisze = [10,20,50];
	/**
	 * 默认
	 */
	this.defaultPageisze = 10;
	/**
	 * 集成的分页控件, 可对表格中的数据集进行客户端分页.
	 * @type PagerView
	 */
	this.pager = {};
	/**
	 * 集成的排序控件, 用于显示分页按钮/链接.
	 * @type SortView
	 */
	this.sort = {};
	/**
	 * 存放检索内容
	 */
	this.queryContent = {};
	/**
	 * 存放只使用一次的请求内容，例如请求的页码
	 */
	this.oneoffContent = {};
	/**
	 * ajax请求的地址
	 */
	this.url = "";
	/**
	 * 显示分页控件
	 */
	this.showPageComp = true;

	this.headerData = [];
	/**
	 * @class
	 * 用于确定要显示哪些内部控件, 控件对应的属性为Boolean类型, 取值为true时显示.
	 */
	function DisplayOptions(){
		/**
		 * 标题
		 * @type Boolean
		 */
		this.title = true;
		/**
		 * 计数
		 * @type Boolean
		 */
		this.count = true;
		/**
		 * 行选择框
		 * @type Boolean
		 */
		this.marker = true;
		/**
		 * 过滤器
		 * @type Boolean
		 */
		this.filter = false;
		/**
		 * 分页
		 * @type Boolean
		 */
		this.pager = false;
		/**
		 * 排序
		 * @type Boolean
		 */
		this.sort = false;
		/**
		 * 是否多选
		 * @type Boolean
		 */
		this.multiple = false;
		/**
		 * 是否单选
		 * @type Boolean
		 */
		this.single = false;
		/**
		 * 调试
		 * @type Boolean
		 */
		this.debug = false;
		
	};

	/**
	 * 用于确定要显示哪些内部控件.
	 * @type TableView-DisplayOptions
	 */
	this.display = new DisplayOptions();
	
	this.initHeader = function(row){
		self.headerData  = row;
		for(var i=0;i<self.headerData.length;i++){
			//注册id
			if(self.headerData[i].key === true){
				this.dataKey = self.headerData[i].id;
			}
			// 注册需要排序的字段
			if(self.headerData[i].order === true){
				this.orderHeader.push(self.headerData[i].id);
			}
			//注册表头
			if(self.headerData[i].id != true){
				if(self.headerData[i].id !== 'marker'){
					this.header[self.headerData[i].id]=self.headerData[i].name;
				}
			}
			//注册宽度
			if(self.headerData[i].width != null){
				this.headerWidth[self.headerData[i].id]=self.headerData[i].width;
			}
			//注册单选多选类型
			if(self.headerData[i].id === 'marker'){
				if(self.headerData[i].type ==='radio'){
					this.display.single = true;
				}else{
					this.display.multiple = true;
				}
			}
			
		}
	};
	this.getHeader = function(id){
		if(self.headerData.length ===0){
			return;
		}
		for(var i=0;i<self.headerData.length;i++){
			if(self.headerData[i].id === id){
				return self.headerData[i];
			}
		}
	}
	
	/**
	 * 获取数据集指定id一条记录.
	 * @returns {Object} 数据集中的一条记录.
	 */
	this.get = function(id){
		return array_get(this.rows, self.dataKey, id);
	};

	/**
	 * 添加一条记录, 如果控件已经被渲染, 会导致一次刷新.
	 * @param {Object} row: 记录对象.
	 */
	this.add = function(row){
		var index = array_index_of_item(self.rows, row);
		if(index != -1){
			return;
		}
		this.rows.push(row);
		this._display_rows.push(row);
		if(self._rendered){
			self.render();
		}
	};

	/**
	 * 添加记录列表, 如果控件已经被渲染, 会导致一次刷新.
	 * 用本方法替代连续多次{@link TableView#add()}, 以提高性能.
	 * @param {Array[Object]} rows: 记录对象的数组.
	 */
	this.addRange = function(rows){

		var index = {};
		for(var i in self.rows){
			var rid = self.rows[i][self.dataKey];
			index[rid] = true;
		}

		for(var i in rows){
			var row = rows[i];
			var rid = row[self.dataKey];

			if(!index[rid]){
				this.rows.push(row);
				this._display_rows.push(row);
			}
		}
		if(self._rendered){
			self.render();
		}
	};

	/**
	 * 删除一个记录对象, 如果控件已经被渲染, 会导致一次刷新.
	 * 可以在调用本方法前, 调用{@link TableView#get()}方法通过id获取要删除的记录对象.
	 * @param {Object} row: 记录对象.
	 */
	this.del = function(row){
		var rid = row[self.dataKey];
		self.rows = array_del(self.rows, self.dataKey, rid);
		self._display_rows = array_del(self._display_rows, self.dataKey, rid);
		if(self._rendered){
			self.render();
		}
	};

	/**
	 * 删除记录对象列表, 如果控件已经被渲染, 会导致一次刷新.
	 * 用本方法替代连续多次{@link TableView#del()}, 以提高性能.
	 * @param {Array[Object]} rows: 记录对象的数组.
	 */
	this.delRange = function(rows){
		var index = {};
		for(var i in rows){
			var rid = rows[i][self.dataKey];
			index[rid] = true;
		}

		var n_rows = [];
		for(var i in self.rows){
			var row = self.rows[i];
			var rid = row[self.dataKey];
			if(!index[rid]){
				n_rows.push(row);
			}
		}
		self.rows = n_rows;

		var n_rows = [];
		for(var i in self._display_rows){
			var row = self._display_rows[i];
			var rid = row[self.dataKey];
			if(!index[rid]){
				n_rows.push(row);
			}
		}
		self._display_rows = n_rows;


		if(self._rendered){
			self.render();
		}
	};

	/**
	 * 内部方法. 用于全选或者取消全选行.
	 */
	this._toggleSelect = function(){
		var c = $(self.container).find('th.marker input[type=checkbox]')[0];
		if(c.checked){
			self.selectAll();
		}else{
			self.unselectAll();
		}
	};

	/**
	 * 使用者重写本方法, 进行行双击回调.
	 * @param {int} id: 双击行的主键值.
	 * @event
	 */
	this.dblclick = function(id){
	};

	/**
	 * 内部方法, 行双击时调用.
	 */
	this._dblclick = function(id){
		self.dblclick(id);
	};

	/**
	 * 获取当前可显示的数据数.
	 * @returns {int}
	 */
	this.rowCount = function(){
		var n = 0;
		for(var i in self._display_rows){
			n ++;
		}
		return n;
	};

	/**
	 * 内部方法, 渲染视图框架.
	 */
	this._render_framework = function(){
		var str = '';
		str += '<div class="TableView">\n';
		str += '<div class="datagrid_meta">\n';
			str += '<span class="title">' + this.title + '</span>';
//			str += '<span class="count">(<span class="marked_count">0</span>/<span class="row_count">0</span>)</span>';
			str += '<span class="filter"><label>正则过滤</label>';
			str += '<input type="text" value="' + this._filter_text + '"'
				+ ' onkeyup="document.getElementById(\'' + this.id + '\').view.filter(this.value)" />';
			str += '</span>\n';
		str += '</div>\n';

		//var datagrid_div_id = self.id + '_datagrid_div__';
		//str += '<div class="datagrid_div" id="' + datagrid_div_id + '">\n';
		str += '<div class="datagrid_div">\n';
		str += '</div><!-- /.datagrid_div -->\n';

		var pager_id = self.id + '_pager__';
		str += '<div id="' + pager_id + '" class="pager"></div>\n';

		// debug
		var debug_div_id = self.id + '_debug';
		str += '<div id="' + debug_div_id + '"></div>\n';

		str += '</div><!-- /.TableView -->\n';

		var div = document.getElementById(self.id);
		if(div==null) return;
		div.view = self;
		self.container = div;
		self.container.innerHTML = str;

		// debug
		self._debug = $('#' + debug_div_id);

		// 捕获异常, 可以不需要PagerView工作
		try{
			self.pager = new PagerView(pager_id);
			self.pager.onclick = function(index){
				self.render();
			};

			self.sort = new SortView();
			self.sort.onclick = function(field, order){
				self.sort.sort(self._display_rows);
				self.render();
			};
		}catch(e){
		}
	};

	self._render_framework();

	/**
	 * 更新统计数据.
	 */
	this._update_meta = function(){
		if(!self.display.count){
			return;
		}
		var marked_count = 0;
		marked_count = $(self.container).find('table.datagrid td.marker input[value!=""]:checked').length;
		$(self.container).find('div.datagrid_meta span.marked_count').html(marked_count);
		$(self.container).find('div.datagrid_meta span.row_count').html(self.rowCount());
	};

	/**
	 * 内部方法. 绑定事件, 设置外观.
	 */
	this._after_render = function(){
		var trs = $(self.container).find('table.datagrid>tbody>tr.tv_row');
		trs.each(function(i, tr){
			var cb = tr.getElementsByTagName('input')[0];

			var clz = i%2==0? 'odd' : 'even';
			// 标记已选的行
			if(cb.checked){
				clz += ' marked';
			}
			$(tr).removeClass('odd even marked');
			$(tr).addClass(clz);

			cb.onclick = function(){
				cb.checked = !cb.checked;
			};

			tr.onclick = function(){
				if(!cb.checked){
					if(!self.display.multiple){
						self.unselectAll();
					}
				}
				cb.checked = !cb.checked;
				if(cb.checked){
					$(this).addClass('marked');
				}else{
					$(this).removeClass('marked');
				}
				self._update_meta();
			};
			tr.onmouseover = function(){
				$(this).addClass('hover');
			};
			tr.onmouseout = function(){
				$(this).removeClass('hover');
			};
			tr.ondblclick = function(){
				self._dblclick(cb.value);
			};
		});

		self._update_meta();

		if(!self.display.title){
			$(self.container).find('div.datagrid_meta span.title').hide();
		}else{
			$(self.container).find('div.datagrid_meta span.title').show();
		}
		if(!self.display.count){
			$(self.container).find('div.datagrid_meta span.count').hide();
		}else{
			$(self.container).find('div.datagrid_meta span.count').show();
		}
		if(!self.display.filter){
			$(self.container).find('div.datagrid_meta span.filter').hide();
		}else{
			$(self.container).find('div.datagrid_meta span.filter').show();
		}
		if(!self.display.pager){
			$('#' + self.pager.id).hide();
		}else{
			$('#' + self.pager.id).show();
		}
		if(!self.display.multiple && !self.display.single){
			$(self.container).find('td.marker, th.marker').hide();
		}else{
			$(self.container).find('td.marker, th.marker').show();
		}
		if(self.display.marker != null){
		
			if(!self.display.marker){
				$(self.container).find('td.marker, th.marker').hide();
			}else{
				$(self.container).find('td.marker, th.marker').show();
			}
		}
	};

	// DEBUG
	function debug(str){
		if(self.display.debug){
			self._debug.css('border', '2px solid #f00');
			self._debug.append(str + '<br/>');
		}
	}

	/**
	 * 渲染控件.
	 */
	this.render = function(){
		var buf = [];
		buf.push('<table class="datagrid table table-hover" ><thead class="fixedThread">\n');
		buf.push('<tr>\n');
		var markerWidth = this.headerWidth["marker"] == null ? 0 : this.headerWidth["marker"];
		var markerAlign = 'center';
		if(self.getHeader('marker')!=null && self.getHeader('marker').align != null){
			markerAlign = self.getHeader('marker').align;;
		}

		if(markerWidth !==0){
			buf.push('<th class="marker" style="text-align:'+markerAlign+'" width="' + markerWidth + '%">');
			if(self.display.multiple){
				buf.push('<input type="checkbox" value="" onclick="document.getElementById(\'' + this.id + '\').view._toggleSelect()" />');
			}
			buf.push('</th>\n');
		}

		for(var k in this.header){
			var width = this.headerWidth[k];
			var needOrder = false;
			for(var i in this.orderHeader){
				if(this.orderHeader[i] === k){
					needOrder = true;
				}
			}
			var markerAlign = 'center';
			if(self.getHeader(k)!=null && self.getHeader(k).align != null){
				markerAlign = self.getHeader(k).align;;
			}
			if(needOrder){
				var orderSpan = '';
				if(self.queryContent['orderitem'] === k && self.queryContent['order'] === 'asc'){
					orderSpan = ' <span class="glyphicon glyphicon-chevron-up"></span>';
				} else if (self.queryContent['orderitem'] === k && self.queryContent['order'] === 'desc'){
					orderSpan = ' <span class="glyphicon glyphicon-chevron-down"></span>';
				} else {
					orderSpan = ' <span class="glyphicon glyphicon-minus"></span>';
				}
				buf.push('<th  class="tableview-title" style="text-align:'+markerAlign+'" field="' + k + '" width="' + width + '%">' + self.header[k] + orderSpan +'</th>\n');
			}else {
				buf.push('<th field="' + k + '" style="text-align:'+markerAlign+'" width="' + width + '%">' + self.header[k] + '</th>\n');
			}
			
		}
		buf.push("</tr>\n</thead>\n");

		if(self.display.sort){
			self.sort.sort(self._display_rows);
		}
		if(self.display.pager){
			self.pager.itemCount = self._display_rows.length;
			var rows = self.pager.page(self._display_rows);	
		}else{
			var rows = self._display_rows;
		}

		buf.push("<tbody>\n");
		for(var i in rows){
			var row = rows[i];
			var rid = row[self.dataKey];

			var markerAlign = 'center';
			var markerWidth = this.headerWidth["marker"] == null ? 0 : this.headerWidth["marker"];
			if(self.getHeader('marker')!=null && self.getHeader('marker').align != null){
				markerAlign = self.getHeader('marker').align;
			}
			
			if(markerWidth !==0){
				var addClass = row["addClass"] == null?"":row["addClass"];
				buf.push('<tr class="tv_row '+addClass+' ">\n<td class="marker" align="'+markerAlign+'">');
				if(self.display.multiple){
					buf.push('<input type="checkbox" value="');
				} else if(self.display.single){
					buf.push('<input type="radio" value="');
				}
				buf.push(rid);
				buf.push('" /></td>\n');
			} else{
				var addClass = row["addClass"] == null?"":row["addClass"];
				buf.push('<tr class="'+addClass+' ">\n');
			}
			for(var k in self.header){

				var markerAlign = 'center';
				if(self.getHeader(k)!=null && self.getHeader(k).align != null){
					markerAlign = self.getHeader(k).align;
				}
				buf.push('<td align="'+markerAlign+'">');
				buf.push(row[k]);
				buf.push('</td>\n');
			}
			buf.push('</tr>\n');
		}
		buf.push("</tbody></table>\n");

		$(self.container).find('div.datagrid_meta span.title').html(this.title);
		
		// $.html() is really slow in IE
		//$(self.container).find('div.datagrid_div').html(buf.join(''));
		$(self.container).find('div.datagrid_div').each(function(i, e){
			e.innerHTML = buf.join('');
		});

		self._after_render();

		if(self.display.pager){
			self.pager.render();
		}

		if(self.display.sort){
			var is_empty = true;
			if(self.sort.fields){
				for(var i in self.sort.fields){
					is_empty = false;
				}
			}
			if(is_empty){
				var fields = {};
				for(var k in self.header){
					fields[k] = [null, null];
				}
				self.sort.fields = fields;
			}
			var elements = {};
			$(self.container).find('table.datagrid th[field]').each(function(i, th){
				var k = $(th).attr('field');
				if(k != undefined){
					elements[k] = th;
				}
			});
			self.sort.render(elements);
		}

        //$('.datagrid').fixedtableheader();

		self._rendered = true;
		bindOrder();
		paginationInit(self.id);
	};

	/**
	 * 设置所有行的选择标记. 如果设置了分页, 则只对当前页有效.
	 */
	this.selectAll = function(){
		$(self.container).find('th.marker input, td.marker input').each(function(i, e){
			e.checked = true;
		});
		self._after_render();
	};

	/**
	 * 取消所有行的选择标记. 如果设置了分页, 则只对当前页有效.
	 */
	this.unselectAll = function(){
		$(self.container).find('th.marker input, td.marker input').each(function(i, e){
			e.checked = false;
		});
		self._after_render();
	};

	/**
	 * 返回所有的记录的列表.
	 * @returns {Array[Object]}
	 */
	this.getDataSource = function(){
		return self.rows;
	};

	/**
	 * 获取所有标记为选择的行对应的记录的列表.
	 * @returns {Array[Object]}
	 */
	this.getSelected = function(){
		var items = [];
		$(self.container).find('.datagrid td.marker input[value!=""]:checked').each(function(i, cb){
			if(cb.checked){
				// 注意, 不要作为hash使用, 否则会导致使用者判断选中个数时错误.
				var row = array_get(self.rows, self.dataKey, cb.value);
				items.push(row);
			}
		});

		return items;
	};

	/**
	 * 获取所有已选择的数据对象键值的列表.
	 * @returns {Array[Key]}
	 */
	this.getSelectedKeys = function(){
		var keys = [];
		var rows = self.getSelected();
		for(var i in rows){
			keys.push(rows[i][self.dataKey]);
		}
		return keys;
	};

	/**
	 * 进行模糊过滤.
	 * @param {String} text: Regex字符串.
	 */
	this.filter = function(text){
		self._filter_text = text;
		self._display_rows = [];

		var regex = new RegExp(text, 'i');
		for(var key in self.rows){
			var row = self.rows[key];
			if(text == ''){
				self._display_rows.push(row);
			}else{
				// 只对看到的进行过滤
				for(var k in self.header){
					var find = String(row[k]).replace(/<[^>]*>/g, '');
					if(regex.test(find)){
						self._display_rows.push(row);
						break;
					}
				}
			}
		}

		if(self.display.pager){
			self.pager.index = 1;
		}
		self.render();
	};

	/**
	 * 清空所有行.
	 */
	this.clear = function(){
		self.rows = [];
		self._display_rows = [];

		if(self.display.pager){
			self.pager.index = 1;
		}
		self.render();
	};
	this.putQueryParam = function(key,value){
		this.queryContent[key] = value;
	};
	this.putOneoffParam = function(key,value){
		this.oneoffContent[key] = value;
	};
	this.clearQueryParam = function(){
		this.queryContent = {};
	};
	this._ajaxUrl = function(pageindex){
		if(this.url == null || this.url === ''){
			return;
		}
		var content = {};
		for(var key in this.queryContent){
			content[key] = this.queryContent[key];
		}
		for(var key in this.oneoffContent){
			content[key] = this.oneoffContent[key];
		}
		// 清空一次性数据
		this.oneoffContent = {};
		$.ajax({
			type:"POST",
			contentType : "application/json;charset=utf-8",
			url : this.url,
			data : JSON.stringify(content),
//			data : content,
			success : function(data,status){
				self.changePage(pageindex,data["totalpage"]);
				self.ajaxCallback(data);
			},
			error : function(){
				self.ajaxFailCallback();
			}
		});
	};
	this.ajaxCallback = function(data){
		alert("未覆写方法ajaxCallback");
	};
	this.ajaxFailCallback = function(){
		alert("未覆写方法ajaxFailCallback");
	};
	
	
	
	/**
	 * 分页控件
	 */
	this.paginationMaxLength=10;//分页栏的最大显示条数
	this.onlyOnePageIsShow = true;//只有一页的时候是否显示
	function paginationInit(id){
	    $('#'+id+'[pagination =pagination_new ]').each(function(){
	        self.initPagination($(this));
	    });
	}
	function isNeedPagination(totalpage,settingfromHTML){
	    var condition ;
	    if(settingfromHTML == "true"){
	        condition = true;
	    }else if(settingfromHTML == 'false'){
	        condition = false;
	    }else {
	        condition = this.onlyOnePageIsShow;
	    }
	    if(condition && totalpage<1){
	        return false;
	    }else if(!condition && totalpage<=1){
	        return false;
	    }
	    return true;
	}
	function setDisplayMaxLength(element,len){
	    var currentpage =  Number(element.attr('pagenumber'));
	    var totoalpage = Number(element.attr('totalpage'));
	    if(checkParamIsPositiveInteger(len)){
	        len = Number(len);
	    }else{
	        len = this.paginationMaxLength;
	    }
	    if(len<totoalpage){
	        var temp1 = parseInt((len-1)/2);
	        var temp2 = parseInt(len/2);
	        if(temp1<temp2){
	            var leftstart = currentpage - temp1;
	            var rightend = currentpage + temp1 + 1;
	        }else{
	            var leftstart = currentpage - temp1;
	            var rightend = currentpage + temp1;
	        }
	        if(leftstart<1){
	            rightend +=(1-leftstart);
	            leftstart = 1;
	        }
	        if(rightend>totoalpage){
	            if(leftstart>1){
	                leftstart -=(rightend-totoalpage);
	            }
	            rightend =totoalpage;
	        }
	        if(leftstart<1){
	            leftstart=1;
	        }
	        while(leftstart >1){
	            element.children('ul').children('li[value = '+(--leftstart)+']').css('display','none');
	        }
	        while(rightend <totoalpage){
	            element.children('ul').children('li[value = '+(++rightend)+']').css('display','none');
	        }
	    }
	}
	//根据页面pagenumber、my_totoalpage更新分页，参数element传的是分页的容器
	this.initPagination = function(element){
		var pageSize = $('#'+id+'-pageSize').find("option:selected").val();
	    element.html('');
	    var pagenumber = Number(element.attr('pagenumber'));
	    var totalpage = Number(element.attr('totalpage'));
	    if(pagenumber === 0){
	    	pagenumber = 1;
	    }
	    if(totalpage === 0){
	    	totalpage = 1;
	    }
	    var pMaxLength = element.attr('paginationMaxLength');
//	    var onePageIsShow = element.attr('onlyOnePageIsShow');
//	    if(isNeedPagination(Number(totalpage),onePageIsShow)){
//	    if(isNeedPagination(Number(totalpage))){
	        var content = '<ul class="pagination"><li class="pagination-txbtn" value="-3"><a href="javascript:void(0);">首页</a></li>';
	        content +='<li class="pagination-txbtn" value="-1"><a href="javascript:void(0);">上一页</a></li>';
	        for(var i =1; i<=totalpage;i++){
	            content +='<li class="paginationNum-txbtn" value="'+i+'"><a href="javascript:void(0);">'+i+'</a></li>';
	        }
	        content +='<li class="pagination-txbtn" value="-2"><a href="javascript:void(0);">下一页</a></li>';
	        content +='<li class="pagination-txbtn" value="-4"><a href="javascript:void(0);">末页</a></li>';
	        var pagesizeOption = '';
	        for(var i in self.pageisze){
	        	if(self.defaultPageisze == self.pageisze[i]){
		        	pagesizeOption += '<option selected value="'+self.pageisze[i]+'">'+self.pageisze[i]+'</option>';
	        	} else {
		        	pagesizeOption += '<option value="'+self.pageisze[i]+'">'+self.pageisze[i]+'</option>';
	        	}
	        }
	        content +='<li class="pagination-txCombo disabled"><a>每页 <select id="'+id+'-pageSize" name="pagesize" class="pagination-txSelect tableview-query-select">'
	        + pagesizeOption +'</select> 条</a></li>';
	        content +='<li class="pagination-txCombo disabled"><a id="'+id+'-pageIndex">当前第'+pagenumber+'/'+totalpage+'页</a></li>';
	        content +='<li class="pagination-txbtn" value="0"><a id="'+id+'-reflesh" href="javascript:void(0);">刷新</a></li></ul>';
	        element.append(content);
	        element.children('ul').children('li[value='+pagenumber+']').addClass('active');
	        setDisplayMaxLength(element,pMaxLength);
	        self.addClickListener(element);
	        if(pageSize != null){
		        $('#'+id+'-pageSize').val(pageSize);
	        }
//	    }
	};
	this.addClickListener = function(element){
		$("#"+id+"-pageSize").on("change",function(){
            changePageIndex(1);
	        var pagenumber = Number($(this).parent().parent().attr('pagenumber'));
            self.paginationClick(element.attr("id"));
            self._ajaxUrl();
            self.initPagination(element);
		});
	    element.children('ul').children('li').bind('click',function(){
	        var temp = Number($(this).attr('value'));
	        var pagenumber = Number($(this).parent().parent().attr('pagenumber'));
	        var totalpage = Number($(this).parent().parent().attr('totalpage'));
		    if(pagenumber === 0){
		    	pagenumber = 1;
		    }
	        if(totalpage===0){
	        	totalpage = 1;
	        }
//	        if(temp === pagenumber){
//	        	return false;
//	        }
	        if(temp === -1){
	            temp = pagenumber-1;
	        }else if(temp === -2){
	            temp = pagenumber+1;
	        }else if(temp === -3){
	        	temp = 1;
	        }else if(temp === -4){
	        	temp = totalpage;
	        }else if(temp === 0){
	        	temp = pagenumber;
	        }
	        if(temp > 0 && temp<=totalpage){
	            $(this).parent().parent().attr('pagenumber',temp);
	            self.paginationClick(element.attr("id"));
	            self._ajaxUrl();
	           self.initPagination(element);
	        }
	        return false;
	    });
	};
	function checkParamIsPositiveInteger(param){
	    var reg = /^[1-9]+[0-9]*]*$/;
	    return reg.test(param);
	};

	this.paginationClick = function(paginationId){
        var pagenumber = $('#'+paginationId+'').attr('pagenumber');
        var totalpage = $('#'+paginationId+'').attr('totalpage');
        var pageSize = $('#'+id+'-pageSize').find("option:selected").val();
        self.putOneoffParam("pageindex",pagenumber);
        self.putOneoffParam("pagesize",pageSize);
	};
	
	/**
	 * 获取所有搜索参数
	 */
	this.getQueryParam = function(){
		$("input.tableview-query-input").each(function(){
			var name = $(this).attr("name");
			var value = $(this).val();
			self.queryContent[name] =  value;
		});
		$("select.tableview-query-select").each(function(){
			var name = $(this).attr("name");
			var value = $(this).find("option:selected").val();
			self.queryContent[name] = value;
		});
	};
	this.changePage = function(pageindex,totalpage){
		var element = $("#"+this.id+"-page");
		// 如果当前页码大于总页码，重新请求最后一页
		if(totalpage!==0 && Number($(element).attr('pagenumber'))>totalpage){
	    	self.putOneoffParam("pageindex",totalpage);
	    	self.putOneoffParam("pagesize",$("#"+id+"-pageSize").find("option:selected").val());
	    	changePageIndex(totalpage);
			self._ajaxUrl();
			return;
		}
		if(pageindex != null){
			$(element).attr('pagenumber',pageindex);
		}
		if(totalpage != null){
			$(element).attr('totalpage',totalpage);
		}
		self.initPagination(element);
	};
	
	// 跳转到指定页(不发请求，仅页码变动)
	function changePageIndex(index){
        $("#"+id+"-page").attr('pagenumber',index);
	}
	
	// 跳转到首页
	this.toFirstPage = function(){
    	self.putOneoffParam("pageindex",1);
    	self.putOneoffParam("pagesize",$("#"+id+"-pageSize").find("option:selected").val());
    	changePageIndex(1);
		self._ajaxUrl();
	};

	// 表头排序
	function bindOrder(){
		$("#"+id).find(".tableview-title").each(function(){
			$(this).on("click",function(){
//		});
//		$("#"+id+".tableview-title").on("click",function(){
			// 移除表头排序图标
			$($(this).parent().find("span")).each(function(){
				$(this).removeClass("glyphicon-chevron-up");
				$(this).removeClass("glyphicon-chevron-down");
			});
			var orderSpan = $(this).find("span");
			var title = $(this).attr("field");
			
			if(self.queryContent["orderitem"] !== title){
				self.queryContent["orderitem"] = title;
				self.queryContent["order"] = "asc";
				$(orderSpan).addClass("glyphicon-chevron-up");
			} else{
				var order = self.queryContent["order"];
				if(order === "asc"){
					self.queryContent["order"] = "desc";
					$(orderSpan).addClass("glyphicon-chevron-down");
				} else {
					$(orderSpan).addClass("glyphicon-minus");
					delete self.queryContent["orderitem"];
					delete self.queryContent["order"];
				}
			}
	    	self.putOneoffParam("pageindex",1);
	    	self.putOneoffParam("pagesize",$("#"+id+"-pageSize").find("option:selected").val());
	    	changePageIndex(1);
			self._ajaxUrl();
			
		});
	});
	}

	$(function(){		
		if(self.showPageComp){
			self.createPage();
		}
    	self.putOneoffParam("pageindex",1);
    	self.putOneoffParam("pagesize",self.pageisze[0]);
		self._ajaxUrl();
		
	    
	    $("."+self.id+"-query-submit").on("click",function(){
	    	self.getQueryParam();
	    	self.putOneoffParam("pageindex",1);
	    	self._ajaxUrl(1);
	    });
	    $("."+self.id+"-query-reset").on("click",function(){
	    	self.clearQueryParam();
	    	$($(".tableview-title").parent().find("span")).each(function(){
				$(this).removeClass("glyphicon-chevron-up");
				$(this).removeClass("glyphicon-chevron-down");
			});
	    	self.clearQueryComp();
	        $('#'+self.id+'-pageSize').val(self.pageisze[0]);
	    	self.putOneoffParam("pageindex",1);
	    	self.putOneoffParam("pagesize",$("#"+id+"-pageSize").find("option:selected").val());
	    	self._ajaxUrl(1);
	    });
	});
	/**
	 * 用户覆写,清空所有tableview-query-input
	 * 和tableview-query-select的值
	 */
	this.clearQueryComp = function(){
		alert("未覆写方法clearQueryComp");
	};
};
