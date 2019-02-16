package net.zhenghao.zh.auth.dao;

import net.zhenghao.zh.common.dao.BaseMapper;
import net.zhenghao.zh.auth.entity.SysRoleMenuEntity;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 系统角色与菜单关系
 *
 * @author:zhaozhenghao
 * @Email :736720794@qq.com
 * @date  :2017年12月6日 上午11:28:45
 * SysRoleMenuMapper.java
 */
@MapperScan
@Component
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenuEntity> {

	int batchRemoveByMenuId(Long[] id);
	
	int batchRemoveByRoleId(Long[] id);
	
	List<Long> listMenuId(Long id);
}