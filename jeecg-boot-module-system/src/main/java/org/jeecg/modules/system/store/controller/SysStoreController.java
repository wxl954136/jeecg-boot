package org.jeecg.modules.system.store.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.system.SystemUtil;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;

import org.jeecg.modules.system.store.entity.SysStore;
import org.jeecg.modules.system.store.service.ISysStoreService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;


import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.utils.SysUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.ModelAndView;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 仓库信息
 * @Author: jeecg-boot
 * @Date:   2020-06-02
 * @Version: V1.0
 */
@Api(tags="仓库信息")
@RestController
@RequestMapping("/store/sysStore")
@Slf4j
public class SysStoreController extends JeecgController<SysStore, ISysStoreService> {
	@Autowired
	private ISysStoreService sysStoreService;
	
	/**
	 * 分页列表查询
	 *
	 * @param sysStore
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "仓库信息-分页列表查询")
	@ApiOperation(value="仓库信息-分页列表查询", notes="仓库信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SysStore sysStore,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		/*
		System.out.println("1=============================");

		Map<String,String[]> mapResult = new HashMap<>();
		mapResult.put("gsdm",new String[]{SysUtils.getLoginUser().getGsdm()});
		mapResult.put("name",new String[]{"三"});
		Map<String,String[]> map=mapResult;
		//遍历
		for(Iterator iter=map.entrySet().iterator();iter.hasNext();) {
			Map.Entry element = (Map.Entry) iter.next();
			//key值
			Object strKey = element.getKey();
			//value,数组形式
			String[] value = (String[]) element.getValue();
			String yyy = " zzzz ==== " +  strKey.toString() + "  :  ";
			//System.out.print(strKey.toString() + "=");
			String result = "";
			for (int i = 0; i < value.length; i++) {
				result = result +  value[i] + "===";
			}
			mapResult.put(strKey.toString(),value);

			System.out.println(yyy + result );
		}

*/
		QueryWrapper<SysStore> queryWrapper = QueryGenerator.initQueryWrapper(sysStore, req.getParameterMap());
		//QueryWrapper<SysStore> queryWrapper = QueryGenerator.initQueryWrapper(sysStore, mapResult);
		Page<SysStore> page = new Page<SysStore>(pageNo, pageSize);
		IPage<SysStore> pageList = sysStoreService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param sysStore
	 * @return
	 */
	@AutoLog(value = "仓库信息-添加")
	@ApiOperation(value="仓库信息-添加", notes="仓库信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SysStore sysStore) {
		sysStoreService.save(sysStore);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param sysStore
	 * @return
	 */
	@AutoLog(value = "仓库信息-编辑")
	@ApiOperation(value="仓库信息-编辑", notes="仓库信息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SysStore sysStore) {
		sysStoreService.updateById(sysStore);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "仓库信息-通过id删除")
	@ApiOperation(value="仓库信息-通过id删除", notes="仓库信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		sysStoreService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "仓库信息-批量删除")
	@ApiOperation(value="仓库信息-批量删除", notes="仓库信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sysStoreService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "仓库信息-通过id查询")
	@ApiOperation(value="仓库信息-通过id查询", notes="仓库信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SysStore sysStore = sysStoreService.getById(id);
		if(sysStore==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(sysStore);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param sysStore
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SysStore sysStore) {
        return super.exportXls(request, sysStore, SysStore.class, "仓库信息");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SysStore.class);
    }

}
