package org.jeecg.modules.system.trader.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.shiro.vo.DefContants;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.trader.entity.SysTrader;
import org.jeecg.modules.system.trader.service.ISysTraderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

 /**
 * @Description: 往来单位
 * @Author: jeecg-boot
 * @Date:   2020-06-02
 * @Version: V1.0
 */
@Api(tags="往来单位")
@RestController
@RequestMapping("/trader/sysTrader")
@Slf4j
public class SysTraderController extends JeecgController<SysTrader, ISysTraderService> {
	@Autowired
	private ISysTraderService sysTraderService;
	 @Autowired
	 private RedisUtil redisUtil;
	 @Autowired
	 HttpServletRequest request;
	/**
	 * 分页列表查询
	 *
	 * @param sysTrader
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "往来单位-分页列表查询")
	@ApiOperation(value="往来单位-分页列表查询", notes="往来单位-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SysTrader sysTrader,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SysTrader> queryWrapper = QueryGenerator.initQueryWrapper(sysTrader, req.getParameterMap());
		Page<SysTrader> page = new Page<SysTrader>(pageNo, pageSize);
		IPage<SysTrader> pageList = sysTraderService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param sysTrader
	 * @return
	 */
	@AutoLog(value = "往来单位-添加")
	@ApiOperation(value="往来单位-添加", notes="往来单位-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SysTrader sysTrader) {
		sysTraderService.save(sysTrader);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param sysTrader
	 * @return
	 */
	@AutoLog(value = "往来单位-编辑")
	@ApiOperation(value="往来单位-编辑", notes="往来单位-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SysTrader sysTrader) {
		sysTraderService.updateById(sysTrader);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "往来单位-通过id删除")
	@ApiOperation(value="往来单位-通过id删除", notes="往来单位-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		sysTraderService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "往来单位-批量删除")
	@ApiOperation(value="往来单位-批量删除", notes="往来单位-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sysTraderService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "往来单位-通过id查询")
	@ApiOperation(value="往来单位-通过id查询", notes="往来单位-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		System.out.println("xc===youlan================" + id);
		SysTrader sysTrader = sysTraderService.getById(id);
		if(sysTrader==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(sysTrader);
	}
	 /**
	  * 根据用户名或手机号查询用户信息
	  * @param
	  * @return
	  */
	 @GetMapping("/querySysTraderList")
	 public Result getTraderList() {
		 String token = request.getHeader(DefContants.X_ACCESS_TOKEN);
		 SysUser loginUser = (SysUser)redisUtil.get(token);
		 //System.out.println("querySysTrader:=====" + loginUser.getGsdm() + "====" + loginUser.getUsername());
		 Result<Map<String, Object>> result = new Result<Map<String, Object>>();
		 Map<String, Object> map = new HashMap<String, Object>();
		 SysTrader sysTrader = new SysTrader();
		 sysTrader.setGsdm(loginUser.getGsdm());
		 return Result.ok(sysTraderService.getTraderList(sysTrader));
	 }
    /**
    * 导出excel
    *
    * @param request
    * @param sysTrader
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SysTrader sysTrader) {
        return super.exportXls(request, sysTrader, SysTrader.class, "往来单位");
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
        return super.importExcel(request, response, SysTrader.class);
    }

}
