package net.zhenghao.zh.auth.dao;

import net.zhenghao.zh.common.dao.BaseMapper;
import net.zhenghao.zh.auth.entity.SysUserRoleEntity;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户与角色关系
 *
 * @author:zhaozhenghao
 * @Email :736720794@qq.com
 * @date  :2017年12月6日 上午11:13:36
 * SysUserRoleMapper.java
 */
@MapperScan
@Component
public interface SysUserRoleMapper extends BaseMapper<SysUserRoleEntity> {

	/**
	 * 用户id下所有角色id
	 * @param userId
	 * @return
	 */
	List<Long> listUserRoleId(Long userId);
	
	int batchRemoveByUserId(Long[] id);
	
	int batchRemoveByRoleId(Long[] id);
	
}