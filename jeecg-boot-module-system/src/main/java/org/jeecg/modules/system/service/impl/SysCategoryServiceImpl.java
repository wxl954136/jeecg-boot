package org.jeecg.modules.system.service.impl;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.constant.FillRuleConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.FillRuleUtil;
import org.jeecg.common.util.YouBianCodeUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysCategory;
import org.jeecg.modules.system.mapper.SysCategoryMapper;
import org.jeecg.modules.system.model.TreeSelectModel;
import org.jeecg.modules.system.service.ISysCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 分类字典
 * @Author: jeecg-boot
 * @Date:   2019-05-29
 * @Version: V1.0
 */
@Service
public class SysCategoryServiceImpl extends ServiceImpl<SysCategoryMapper, SysCategory> implements ISysCategoryService {

	@Autowired
	private SysCategoryMapper sysCategoryMapper;
	@Override
	public void addSysCategory(SysCategory sysCategory) {
		String categoryCode = "";
		String categoryFullName = sysCategory.getName() + ".";
		String categoryPid = ISysCategoryService.ROOT_PID_VALUE;
		String parentCode = null;

		if(oConvertUtils.isNotEmpty(sysCategory.getPid())){
			categoryPid = sysCategory.getPid();
			//PID 不是根节点 说明需要设置父节点 hasChild 为1
			if(!ISysCategoryService.ROOT_PID_VALUE.equals(categoryPid)){
				SysCategory parent = baseMapper.selectById(categoryPid);
				categoryFullName = parent.getFullName() + sysCategory.getName() + ".";
				parentCode = parent.getCode();
				if(parent!=null && !"1".equals(parent.getHasChild())){
					parent.setHasChild("1");
					baseMapper.updateById(parent);

				}
			}
		}
		//update-begin--Author:baihailong  Date:20191209 for：分类字典编码规则生成器做成公用配置
		JSONObject formData = new JSONObject();
		formData.put("pid",categoryPid);
		categoryCode = (String) FillRuleUtil.executeRule(FillRuleConstant.CATEGORY,formData);
		//update-end--Author:baihailong  Date:20191209 for：分类字典编码规则生成器做成公用配置
		sysCategory.setCode(categoryCode);
		sysCategory.setPid(categoryPid);
		sysCategory.setFullName(categoryFullName);
		baseMapper.insert(sysCategory);
	}
	
	@Override
	public void updateSysCategory(SysCategory sysCategory) {
		String fullName = "";
		if(oConvertUtils.isEmpty(sysCategory.getPid())){
			sysCategory.setPid(ISysCategoryService.ROOT_PID_VALUE);
			fullName = sysCategory.getName() + ".";
		}else{
			//如果当前节点父ID不为空 则设置父节点的hasChild 为1
			SysCategory parent = baseMapper.selectById(sysCategory.getPid());
			if(parent!=null && !"1".equals(parent.getHasChild())){
				parent.setHasChild("1");
				baseMapper.updateById(parent);
			}

			fullName = (null!=parent && null!=parent.getFullName()?parent.getFullName():"") + sysCategory.getName() +".";

		}
		sysCategory.setFullName(fullName);
		baseMapper.updateById(sysCategory);
		updateChildFullName(sysCategory,fullName);

	}
	//递归更新所有的子节点
	public void updateChildFullName(SysCategory sysCategory,String  parentFullName)
	{
		//表面是根结点
//		if("0".equalsIgnoreCase(ISysCategoryService.ROOT_PID_VALUE)){
//			return;
//		}
		if(oConvertUtils.isEmpty(sysCategory.getHasChild())){
			return ;
		}
		if("1".equalsIgnoreCase(sysCategory.getHasChild())){
			//这里会有多个记录集合
			List<SysCategory> listCategorys = sysCategoryMapper.queryListSysCategoryByPid(sysCategory.getId());
			for (SysCategory entity:listCategorys)
			{
				String _fullName = parentFullName + entity.getName() + ".";
				entity.setFullName(_fullName);
				sysCategoryMapper.updateById(entity);
				updateChildFullName(entity,_fullName);
			}
		}


	}
	@Override
	public List<TreeSelectModel> queryListByCode(String pcode) throws JeecgBootException{
		String pid = ROOT_PID_VALUE;
		if(oConvertUtils.isNotEmpty(pcode)) {
			List<SysCategory> list = baseMapper.selectList(new LambdaQueryWrapper<SysCategory>().eq(SysCategory::getCode, pcode));
			if(list==null || list.size() ==0) {
				throw new JeecgBootException("该编码【"+pcode+"】不存在，请核实!");
			}
			if(list.size()>1) {
				throw new JeecgBootException("该编码【"+pcode+"】存在多个，请核实!");
			}
			pid = list.get(0).getId();
		}
		return baseMapper.queryListByPid(pid,null);
	}

	@Override
	public List<TreeSelectModel> queryListByPid(String pid) {
		if(oConvertUtils.isEmpty(pid)) {
			pid = ROOT_PID_VALUE;
		}
		return baseMapper.queryListByPid(pid,null);
	}

	@Override
	public List<TreeSelectModel> queryListByPid(String pid, Map<String, String> condition) {
		if(oConvertUtils.isEmpty(pid)) {
			pid = ROOT_PID_VALUE;
		}
		return baseMapper.queryListByPid(pid,condition);
	}

	@Override
	public String queryIdByCode(String code) {
		return baseMapper.queryIdByCode(code);
	}

}
