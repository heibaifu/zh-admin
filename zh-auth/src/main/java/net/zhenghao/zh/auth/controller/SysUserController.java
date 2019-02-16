package net.zhenghao.zh.auth.controller;

import net.zhenghao.zh.common.annotation.SysLog;
import net.zhenghao.zh.common.constant.SystemConstant;
import net.zhenghao.zh.common.controller.AbstractController;
import net.zhenghao.zh.common.entity.Page;
import net.zhenghao.zh.common.entity.R;
import net.zhenghao.zh.common.entity.SysUserEntity;
import net.zhenghao.zh.auth.service.SysUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 系统用户controller
 * @RequestParam 用来处理Content-Type: 为 application/x-www-form-urlencoded编码的内容
 * @RequestBody	处理HttpEntity传递过来的数据，一般用来处理非Content-Type: application/x-www-form-urlencoded编码格式的数据。
 *
 * @author:zhaozhenghao
 * @Email :736720794@qq.com
 * @date  :2017年12月8日 下午2:03:22
 * SysMenuController.java
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {

	@Resource
	private SysUserService sysUserService;

    /**
     * 获取登录用户信息
     * @return
     */
    @GetMapping("/info")
    public R info() {
        return sysUserService.getUserById(getUserId());
    }

	/**
	 * 获取登录用户权限
	 * @return
	 */
	@RequestMapping("/button")
	public R listUserButton() {
		return sysUserService.listUserButton(getUserId());
	}

	/**
	 * 获取用户信息列表
	 * @return
	 */
	@GetMapping("")
	public Page<SysUserEntity> list(@RequestParam Map<String, Object> params) {
		if (getUserId() != SystemConstant.SUPER_ADMIN) {
			params.put("creatorId", getUserId());
		}
		return sysUserService.listUser(params);
	}

	/**
	 * 获取用户信息
	 * @return
	 */
	@GetMapping("/{id}")
	public R show(@PathVariable("id") Long userId) {
		return sysUserService.getUserById(userId);
	}

	/**
	 * 新增用户
	 * @param user
	 * @return
	 */
	@SysLog("新增用户")
	@PostMapping("")
	public R save(@RequestBody SysUserEntity user) {
		user.setCreatorId(getUserId());
		return sysUserService.saveUser(user);
	}

	/**
	 * 修改用户
	 * @param user
	 * @return
	 */
	@SysLog("修改用户")
	@PutMapping("/{id}")
	public R edit(@PathVariable("id") Long userId ,@RequestBody SysUserEntity user) {
		user.setUserId(userId);
		user.setModifierId(getUserId());
		return sysUserService.updateUser(user);
	}

	/**
	 * 删除
	 * @return
	 */
	@SysLog("删除用户")
	@DeleteMapping("/{id}")
	public R remove(@PathVariable("id") Long userId) {
		return sysUserService.removeUser(userId);
	}

	/**
	 * 用户修改密码
	 * @param pswd
	 * @param newPswd
	 * @return
	 */
	@SysLog("修改密码")
	@RequestMapping("/updatePswd")
	public R updatePswdByUser(String pswd, String newPswd) {
		SysUserEntity user = (SysUserEntity) sysUserService.getUserById(getUserId()).get("data");
		user.setPassword(pswd);//原密码
		user.setEmail(newPswd);//邮箱临时存储新密码
		return sysUserService.updatePswdByUser(user);
	}

	/**
	 * 启用账户
	 * @param id
	 * @return
	 */
	@SysLog("启用账号")
	@RequestMapping("/enable")
	public R updateUserEnable(@RequestBody Long[] id) {
		return sysUserService.updateUserEnable(id);
	}

	/**
	 * 禁用账户
	 * @param id
	 * @return
	 */
	@SysLog("禁用账户")
	@RequestMapping("/disable")
	public R updateUserDisable(@RequestBody Long[] id) {
		return sysUserService.updateUserDisable(id);
	}

	/**
	 * 重置密码
	 * @param user
	 * @return
	 */
	@SysLog("重置密码")
	@RequestMapping("/reset")
	public R updatePswd(@RequestBody SysUserEntity user) {
		return sysUserService.updatePswd(user);
	}
	
}