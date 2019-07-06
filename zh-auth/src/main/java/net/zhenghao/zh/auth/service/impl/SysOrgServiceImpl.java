package net.zhenghao.zh.auth.service.impl;

import net.zhenghao.zh.auth.dao.SysOrgMapper;
import net.zhenghao.zh.auth.dao.SysUserMapper;
import net.zhenghao.zh.auth.entity.SysOrgEntity;
import net.zhenghao.zh.auth.service.SysOrgService;
import net.zhenghao.zh.common.constant.SystemConstant;
import net.zhenghao.zh.common.entity.Result;
import net.zhenghao.zh.common.util.TreeUtils;
import net.zhenghao.zh.common.vo.TreeVO;
import net.zhenghao.zh.common.entity.Page;
import net.zhenghao.zh.common.entity.Query;
import net.zhenghao.zh.common.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 系统组织
 *
 * @author:zhaozhenghao
 * @Email :736720794@qq.com
 * @date :2017年12月7日 下午2:51:12
 * SysOrgServiceImpl.java
 */
@Service
@Transactional
public class SysOrgServiceImpl implements SysOrgService {

    @Autowired
    private SysOrgMapper sysOrgMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public Page<SysOrgEntity> listOrg(Map<String, Object> params) {
        Query query = new Query(params);
        Page<SysOrgEntity> page = new Page<>(query);
        query.removePageParams();
        page.setData(TreeUtils.build(sysOrgMapper.list(query), SystemConstant.TREE_ROOT));
        return page;
    }

    @Override
    public Result<List<TreeVO>> listTree(Map<String, Object> params) {
        List<TreeVO> listRoot;
        if (params.get("isRoot") != null && "true".equals(params.get("isRoot"))) {
            List<TreeVO> treeList = sysOrgMapper.listTree();
            TreeVO root = new TreeVO();
            root.setId(0L);
            root.setKey(0L);
            root.setTitle("主目录");
            root.setValue("0");
            root.setParentId(-1L);
            treeList.add(root);
            listRoot = TreeUtils.build(treeList, -1L);
        } else {
            listRoot = TreeUtils.build(sysOrgMapper.listTree(), SystemConstant.TREE_ROOT);
        }
        return CommonUtils.msgNotNull(listRoot);
    }

    @Override
    public Result saveOrg(SysOrgEntity org) {
        SysOrgEntity orgParent = sysOrgMapper.getObjectById(org.getParentId());
        String ancestors;
        if (orgParent != null) {
            ancestors = orgParent.getAncestors() + "," + orgParent.getId();
        } else {
            ancestors = org.getParentId() + "";
        }
        org.setAncestors(ancestors);
        int count = sysOrgMapper.save(org);
        return CommonUtils.msg(count);
    }

    @Override
    public Result<SysOrgEntity> getOrgById(Long id) {
        SysOrgEntity org = sysOrgMapper.getObjectById(id);
        return CommonUtils.msg(org);
    }

    @Override
    public Result updateOrg(SysOrgEntity org) {
        SysOrgEntity oldOrg = sysOrgMapper.getObjectById(org.getId());
        // 更新前先对比和之前的parentId是否有不同，如果不同则开始处理
        if (!org.getParentId().equals(oldOrg.getParentId())) {
            String ancestors;
            SysOrgEntity orgParent = sysOrgMapper.getObjectById(org.getParentId());
            if (orgParent != null) {
                ancestors = orgParent.getAncestors() + "," + orgParent.getId();
            } else {
                ancestors = org.getParentId() + "";
            }
            org.setAncestors(ancestors);
            // 更新所有子列表的 ancestors 字段
            Query query = new Query();
            query.put("oldAncestors", oldOrg.getAncestors());
            query.put("newAncestors", ancestors);
            query.put("id", org.getId());
            sysOrgMapper.updateChildAncestorsById(query);
        }
        int count = sysOrgMapper.update(org);
        return CommonUtils.msg(count);
    }

    @Override
    public Result remove(Long id) {
        int childCount = sysOrgMapper.countChildById(id);
        if (childCount > 0) {
            return Result.ofFail("该组织含有子组织,请先删除子组织!");
        }
        int userCount = sysUserMapper.countByOrgId(id);
        if (userCount > 0) {
            return Result.ofFail("该组织含有用户,请先处理组织下的用户!");
        }
        int count = sysOrgMapper.remove(id);
        return CommonUtils.msg(count);
    }

}
