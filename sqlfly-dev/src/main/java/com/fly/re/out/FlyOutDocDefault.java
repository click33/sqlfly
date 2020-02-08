package com.fly.re.out;

import com.fly.re.CodeCfg;
import com.fly.re.model.Column;
import com.fly.re.model.MkTable;

/**
 * 生成文档 
 * @author kong
 *
 */
public class FlyOutDocDefault implements FlyOutDoc {

	// 配置信息
	CodeCfg cc;

	public FlyOutDoc setCodeCfg(CodeCfg codeCfg){
		this.cc = codeCfg;
		return this;
	}

	// 生成文档 add 
	@Override
	public String mkAdd(MkTable table) {
		String title = "1、增";
		String api = "/" + table.getClassName() + "/add";
		String args_str = "";
		for (Column column : table.columnList) {
			args_str += "\t" + column.name + "\t\t\t" + column.comment + "\r\n";
		}
		String return_str = "\t成功code=200，失败code=500 \r\n";
		return OutUtil.fzDoc(title, api, args_str, return_str);
	}

	// 生成文档 delete  
	@Override
	public String mkDelete(MkTable table) {
		String title = "2、删";
		String api = "/" + table.getClassName() + "/delete";
		String args_str = "\tid\t\t\t要删除的id\r\n";
		String return_str = "\t成功code=200，失败code=500 \r\n";
		return OutUtil.fzDoc(title, api, args_str, return_str);
	}

	// 生成文档 update  
	@Override
	public String mkUpdate(MkTable table) {
		String title = "3、改";
		String api = "/" + table.getClassName() + "/update";
		String args_str = "";
		for (Column column : table.columnList) {
			args_str += "\t" + column.name + "\t\t\t" + column.comment + "\r\n";
		}
		String return_str = "\t成功code=200，失败code=500 \r\n";
		return OutUtil.fzDoc(title, api, args_str, return_str);
	}

	// 生成文档 getById  
	@Override
	public String mkGetById(MkTable table) {
		String title = "4、查";
		String api = "/" + table.getClassName() + "/getById";
		String args_str = "\tid\t\t\t要查询的id\r\n";
		String return_str = "\t{\r\n";
		return_str += "\t\t\"code\": 200,\r\n";
		return_str += "\t\t\"msg\": \"ok\",\r\n";
		return_str += "\t\t\"data\": {\r\n";
		for (Column column : table.columnList) {
			return_str += "\t\t\t\"" + column.name + "\": \"\",\t\t// " + column.comment + " \r\n";
		}
		return_str += "\t\t}, \r\n";
		return_str += "\t\t\"page\": null \r\n";
		return_str += "\t} \r\n";
		return OutUtil.fzDoc(title, api, args_str, return_str);
	}

	// 生成文档 getList 
	@Override
	public String mkGetList(MkTable table) {
		String title = "5、查 - 集合 ";
		String api = "/" + table.getClassName() + "/getList";
		String args_str = "";
		args_str += "\tpageNo\t\t\t当前页\r\n";
		args_str += "\tpageSize\t\t\t页大小\r\n";
		for (Column column : table.columnList) {
			args_str += "\t" + column.name + "\t\t\t" + column.comment + "\r\n";
		}
		args_str += "\tsort_type\t\t\t排序方式（）\r\n";
		args_str += "\t- 以上所有条件不填写的情况下均代表忽略此条件\r\n";
		String return_str = 
				"\t\t{\r\n" + 
				"\t\t\"code\": 200,\r\n" + 
				"\t\t\t\"msg\": \"ok\",\r\n" + 
				"\t\t\t\"data\": [\r\n" + 
				"\t\t\t\t// 数据列表，格式参考见 getById  \r\n" + 
				"\t\t\t],\r\n" + 
				"\t\t\t\"page\": {\r\n" + 
				"\t\t\t\t\"pageNo\": 1,	// 当前页\r\n" + 
				"\t\t\t\t\"pageSize\": 10,	// 页大小\r\n" + 
				"\t\t\t\t\"start\": 0,\r\n" + 
				"\t\t\t\t\"count\": 1,\r\n" + 
				"\t\t\t\t\"pageCount\": 1	// 可以分多少页\r\n" + 
				"\t\t\t}\r\n" + 
				"\t\t}\r\n";
		return OutUtil.fzDoc(title, api, args_str, return_str);
	}

	// 开始制作 
	@Override
	public String mkIO() {
		String tips = "\n\n=============== markdown 接口文档 共计：(" + cc.tableList.size() + ") ======================";
		System.out.println(tips);

		int i = 0;
		for (MkTable table : cc.tableList) {
			i++;

			String doc = "# " + table.getClassName() + " " + table.comment + " 相关\r\n\r\n";
			doc += mkAdd(table);
			doc += mkDelete(table);
			doc += mkUpdate(table);
			doc += mkGetById(table);
			doc += mkGetList(table);

			OutUtil.outFile(cc.getDocIOPath() + "\\" + i + "-" + table.getClassName() + "-" + table.comment + ".md",
					doc);
			System.out.println(table.name + "\t --> markdown 接口文档 写入成功！");
		}
		return null;
	}

}
