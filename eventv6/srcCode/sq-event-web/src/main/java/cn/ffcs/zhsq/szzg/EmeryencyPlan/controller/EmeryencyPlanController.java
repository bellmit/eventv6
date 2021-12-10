package cn.ffcs.zhsq.szzg.EmeryencyPlan.controller;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.zhsq.mybatis.domain.szzg.EmeryencyPlan.EmeryencyPlanContent;
import cn.ffcs.zhsq.mybatis.domain.szzg.EmeryencyPlan.EmeryencyPlanTree;
import cn.ffcs.zhsq.szzg.EmeryencyPlan.service.EmeryencyPlanContentService;
import cn.ffcs.zhsq.szzg.EmeryencyPlan.service.EmeryencyPlanTreeService;
import cn.ffcs.zhsq.utils.message.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 应急预案 2015-7-24  huangwenbin
 */
@Controller
@RequestMapping(value = "/zhsq/szzg/emeryencyplan")

public class EmeryencyPlanController {
    @Autowired
    EmeryencyPlanTreeService emeryencyPlanTreeService;

    @Autowired
    EmeryencyPlanContentService emeryencyPlanContentService;

    // 模块路径
    private final static String REAL_PATH = "/szzg/";
    // 模块名称
    private final static String SUB_MAIN = "EmeryencyPlan";

    //树首页
    @RequestMapping("/treeIndex")
    public String treeIndex(ModelMap model) {

        model.addAttribute("treeId", 0);
        return REAL_PATH + SUB_MAIN + "/index_tree.ftl";
    }

    //树首页
    @RequestMapping("/contentIndex")
    public String contentIndex(ModelMap model) {
        model.addAttribute("treeId", 0);
        return REAL_PATH + SUB_MAIN + "/index_emeryency.ftl";
    }

    //应急预案前台展示
    @RequestMapping(value = "showIndex")
    public String showIndex(HttpSession session, ModelMap model) {
        return REAL_PATH + SUB_MAIN + "/yjya.ftl";
    }


    /**
     * 后台编辑 获取预案
     */
    @ResponseBody
    @RequestMapping("/findByTreeId")
    public Map<String, Object> findByTreeId(HttpServletRequest request, HttpSession session, Long treeid) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("treeid", treeid);

        List<EmeryencyPlanContent> emeryencyPlanContents = emeryencyPlanContentService.findByTreeId(param);
        List<EmeryencyPlanContent> emeryencyPlanContentList = new ArrayList<EmeryencyPlanContent>();

        param.put("emeryencyPlanContents", emeryencyPlanContentList);

        param.put("content", emeryencyPlanContents);

        return param;
    }

    /**
     * 预案内容页面
     *
     * @param session
     * @param request
     * @param map
     * @return
     */
    @RequestMapping(value = "framePage")
    public String framePage(HttpSession session, HttpServletRequest request, ModelMap map, Long treeId) {
        map.addAttribute("treeId", treeId);
        return REAL_PATH + SUB_MAIN + "/yjya_content.ftl";
    }
    
    @RequestMapping(value = "framePageTwo")
    public String framePageTwo(HttpSession session, HttpServletRequest request, ModelMap map, Long treeId) {
        map.addAttribute("treeId", treeId);
        return REAL_PATH + SUB_MAIN + "/emergrncyPlan_content.ftl";
    }


    @RequestMapping(value = "contentAdd")
    public String contentAdd(HttpSession session, HttpServletRequest request, ModelMap map, Long id) {
        Map<String, Object> params = new HashMap<String, Object>();
        if (id != null) {
            map.addAttribute("id", id);
        }

        return REAL_PATH + SUB_MAIN + "/add_emeryency.ftl";
    }


    /**
     * 后台编辑 内容添加
     */
    @ResponseBody
    @RequestMapping("/insertContent")
    public Map<String, Object> insertContent(HttpServletRequest request,
                                             HttpSession session, EmeryencyPlanContent e, String content) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        boolean result = false;
        if(content!=null&&content!="")
        {
            e.setTxt(content.getBytes());
        }

        if (e.getStatus() == null) {
            e.setStatus("0");
        } else {
            e.setStatus("1");
        }

        try {
            result = emeryencyPlanContentService.insert(e);
            result = e.getId() > 0;
            resultMap.put("msg", Msg.ADD.getMsg(result));
        } catch (Exception e1) {

            e1.printStackTrace();
        }
        return resultMap;
    }

    @RequestMapping(value = "contentEdit")
    public String contentEdit(HttpSession session, HttpServletRequest request, ModelMap map, Long id) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        List<EmeryencyPlanContent> emeryencyPlanContents = emeryencyPlanContentService.findById(params);

        if (emeryencyPlanContents != null && emeryencyPlanContents.get(0) != null) {
            EmeryencyPlanContent emeryencyPlanContent = emeryencyPlanContents.get(0);

            map.addAttribute("emeryencyPlanContent", emeryencyPlanContent);
        }


        return REAL_PATH + SUB_MAIN + "/edit_emeryency.ftl";
    }

    /**
     * 后台编辑 更新
     */
    @ResponseBody
    @RequestMapping("/updateContent")
    public Map<String, Object> updateContent(HttpServletRequest request,
                                             HttpSession session,String content, EmeryencyPlanContent emeryencyPlanContent) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        boolean result = false;

        if(content!=null&&content!="")
        {
            emeryencyPlanContent.setTxt(content.getBytes());
        }

        if (emeryencyPlanContent.getStatus() == null) {
            emeryencyPlanContent.setStatus("0");
        } else {
            emeryencyPlanContent.setStatus("1");
        }




        try {


            result = emeryencyPlanContentService.update(emeryencyPlanContent);
            resultMap.put("msg", Msg.EDIT.getMsg(result));
        } catch (Exception e1) {

            e1.printStackTrace();
        }

        return resultMap;
    }

    /**
     * 后台编辑 获取预案列表
     */
    @ResponseBody
    @RequestMapping("/findListByParams")
    public List<EmeryencyPlanContent> findListByParams(HttpServletRequest request, HttpSession session, String contentId, String title) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        if (contentId != null && contentId != "") {
            param.put("treeId", contentId);
        }


        if (title != null && title != "") {
            param.put("title", title);
        }


        List<EmeryencyPlanContent> emeryencyPlanContents = emeryencyPlanContentService.findByParams(param);


        return emeryencyPlanContents;
    }

    /**
     * 预案详情
     */
    @RequestMapping("/contentDetail")
    public String contentDetail(HttpServletRequest request,HttpSession session,ModelMap map,@RequestParam(value = "id") Long id)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id",id);
        List<EmeryencyPlanContent> emeryencyPlanContents = emeryencyPlanContentService.findById(params);
        if(emeryencyPlanContents.size()!=0)
        {
            map.addAttribute("emeryencyPlanContent",emeryencyPlanContents.get(0));
        }
        return REAL_PATH + SUB_MAIN + "/detail_emeryency.ftl";
    }
    /**
     * 分页取内容列表
     */
    @RequestMapping(value = "/contentList")
    @ResponseBody
    public EUDGPagination contentList(HttpServletRequest request, HttpSession session, String contentId, String title, EmeryencyPlanContent emeryencyPlanContent,
                                      @RequestParam(value = "page") int page,
                                      @RequestParam(value = "rows") int rows) {

        if (page <= 0)
            page = 1;


        Map<String, Object> params = new HashMap<String, Object>();


        if (contentId != null && contentId != "") {
            params.put("treeId", contentId);
        }

        if (title != null && title != "") {
            params.put("title", title);
        }
        EUDGPagination eudgPagination = emeryencyPlanContentService.findPageListByCriteria(params, page, rows);
        return eudgPagination;


    }


    /**
     * 后台编辑 获取树
     */
    @ResponseBody
    @RequestMapping("/fingdTree")
    public List<EmeryencyPlanTree> fingdTree(HttpServletRequest request,
                                             HttpSession session, String key, String parentId) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        if (parentId != null) {
            params.put("parentId", parentId);
            return emeryencyPlanTreeService.fingdTreeByParent(params);
        } else {
            if (key != null && key != "") {
                params.put("key", key);
            }
            List<EmeryencyPlanTree> emeryencyPlanTrees = emeryencyPlanTreeService.fingdTree(params);
            return emeryencyPlanTrees;
        }

    }

    /**
     * 分页取树节点
     */
    @RequestMapping(value = "/treeList")
    @ResponseBody
    public EUDGPagination treeList(HttpServletRequest request, HttpSession session, String key, String parentId,
                                   @RequestParam(value = "page") int page,
                                   @RequestParam(value = "rows") int rows) {

        if (page <= 0)
            page = 1;


        Map<String, Object> params = new HashMap<String, Object>();
        if (key != null && key != "") {
            params.put("key", key);
        }
        if (parentId != null) {
            params.put("parentId", parentId);
        }

        EUDGPagination eudgPagination = emeryencyPlanTreeService.findPageListByCriteria(params, page, rows);
        return eudgPagination;
    }

    /**
     * 后台编辑 获取树
     */
    @ResponseBody
    @RequestMapping("/treeListData")
    public List<EmeryencyPlanTree> treeListData(HttpServletRequest request,
                                                HttpSession session, EmeryencyPlanTree emeryencyPlanTree) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("id", emeryencyPlanTree.getId());
        if (emeryencyPlanTree.getName() != null && emeryencyPlanTree.getName() != "") {
            params.put("name", emeryencyPlanTree.getName());
        }

        return emeryencyPlanTreeService.fingdTree(params);
    }


    /**
     * 后台编辑 根据ParentId获取树
     */
    @ResponseBody
    @RequestMapping("/fingdTreeByParentId")
    public List<EmeryencyPlanTree> fingdTreeByParentId(HttpServletRequest request,
                                                       HttpSession session) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("parentId", request.getParameter("parentId"));
        return emeryencyPlanTreeService.fingdTreeByParent(map);
    }


    /**
     * 树节点编辑页面
     */
    @RequestMapping("/treeEdit")
    public String treeEdit(HttpSession session, HttpServletRequest request, ModelMap map, String id) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        EmeryencyPlanTree emeryencyPlanTree = emeryencyPlanTreeService.findTreeById(params);
        if (emeryencyPlanTree != null) {
            map.addAttribute("emeryencyPlanTree", emeryencyPlanTree);
        }
        return REAL_PATH + SUB_MAIN + "/edit_tree.ftl";
    }

    /**
     * 后台编辑 更新
     */
    @ResponseBody
    @RequestMapping("/updateTree")
    public Map<String, Object> updateTree(HttpServletRequest request,
                                          HttpSession session, EmeryencyPlanTree emeryencyPlanTree) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("parentId", emeryencyPlanTree.getId());
        Map<String, Object> resultMap = new HashMap<String, Object>();
        boolean result = false;
        if (emeryencyPlanTree.getStatus() ==null) {

            List<EmeryencyPlanTree> emeryencyPlanTrees = emeryencyPlanTreeService.fingdTreeByParent(params);
            if (emeryencyPlanTrees != null) {
                for (EmeryencyPlanTree e : emeryencyPlanTrees) {

                    if(e.getId()==emeryencyPlanTree.getId())
                    {
                       e.setId(emeryencyPlanTree.getId());
                        e.setName(emeryencyPlanTree.getName());
                        e.setPriority(emeryencyPlanTree.getPriority());
                       e.setStatus("0");
                    }else
                    {
                        e.setStatus("0");
                    }
                }
            }

            try {

                for (EmeryencyPlanTree e : emeryencyPlanTrees) {
                    result = emeryencyPlanTreeService.update(e);
                }

                resultMap.put("msg", Msg.EDIT.getMsg(result));
            } catch (Exception e1) {

                e1.printStackTrace();
            }

        } else {
            List<EmeryencyPlanTree> emeryencyPlanTrees = emeryencyPlanTreeService.fingdTreeByParent(params);
            if (emeryencyPlanTrees != null) {
                for (EmeryencyPlanTree e : emeryencyPlanTrees) {
                    if(e.getId().equals(emeryencyPlanTree.getId()))
                    {
                        e.setId(emeryencyPlanTree.getId());
                        e.setName(emeryencyPlanTree.getName());
                        e.setPriority(emeryencyPlanTree.getPriority());
                        e.setStatus("1");
                    }else
                    {
                        e.setStatus("1");
                    }
                }
            }



            try {

                for (EmeryencyPlanTree e : emeryencyPlanTrees) {
                    result = emeryencyPlanTreeService.update(e);
                }


                resultMap.put("msg", Msg.EDIT.getMsg(result));
            } catch (Exception e1) {

                e1.printStackTrace();
            }
        }

        return resultMap;
    }


    /**
     * 树节点新增页面
     */
    @RequestMapping("/treeAdd")
    public String treeAdd(HttpSession session, HttpServletRequest request, ModelMap map, Long id) {

        Map<String, Object> params = new HashMap<String, Object>();
        if (id != null) {
            params.put("id", id);
        }

        List<EmeryencyPlanTree> emeryencyPlanTree = emeryencyPlanTreeService.fingdTree(params);
        if (emeryencyPlanTree.size() != 0) {
            map.addAttribute("name", emeryencyPlanTree.get(0).getName());
        }

        map.addAttribute("id", id);

        return REAL_PATH + SUB_MAIN + "/add_tree.ftl";
    }

    /**
     * 后台编辑 添加
     */
    @ResponseBody
    @RequestMapping("/insertTree")
    public Map<String, Object> insertTree(HttpServletRequest request,
                                          HttpSession session, EmeryencyPlanTree emeryencyPlanTree) throws Exception {
        if (emeryencyPlanTree.getStatus() == null) {
            emeryencyPlanTree.setStatus("0");
        } else {
            emeryencyPlanTree.setStatus("1");
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        boolean result = false;
        try {
            result = emeryencyPlanTreeService.insert(emeryencyPlanTree);
            result = emeryencyPlanTree.getId() > 0;
            resultMap.put("msg", Msg.ADD.getMsg(result));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return resultMap;
    }


    /**
     * 后台编辑 删除树节点
     */
    @ResponseBody
    @RequestMapping("/deleteTree")
    public Map<String, Object> deleteTree(HttpServletRequest request,
                                          HttpSession session, Long id,Boolean del) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        if(del!=null&&del)
        {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("treeId",id);
            List<EmeryencyPlanContent> emeryencyPlanContents = emeryencyPlanContentService.findByParams(params);
            boolean isSuccess = emeryencyPlanTreeService.delete(id);
            param.put("parentId",id);
            List<EmeryencyPlanTree> emeryencyPlanTrees = emeryencyPlanTreeService.fingdTreeByParent(param);
            if(emeryencyPlanTrees.size()!=0)
            {
                for(EmeryencyPlanTree emeryencyPlanTree:emeryencyPlanTrees)
                {
                    isSuccess = emeryencyPlanTreeService.delete(emeryencyPlanTree.getId());
                }
            }
            if(emeryencyPlanContents.size()!=0)
            {
               for(EmeryencyPlanContent emeryencyPlanContent:emeryencyPlanContents)
               {
                 isSuccess = emeryencyPlanContentService.delete(emeryencyPlanContent.getId());
               }
            }
            Map<String, Object> resultMap = new HashMap<String, Object>();
            if (isSuccess) {
                resultMap.put("result", 1);
            } else {
                resultMap.put("result", 0);
            }

            resultMap.put("msg", Msg.DELETE.getMsg(isSuccess));
            return resultMap;

        }else
        {
            boolean isSuccess = emeryencyPlanTreeService.delete(id);
            param.put("parentId",id);
            List<EmeryencyPlanTree> emeryencyPlanTrees = emeryencyPlanTreeService.fingdTreeByParent(param);
            if(emeryencyPlanTrees.size()!=0)
            {
                for(EmeryencyPlanTree emeryencyPlanTree:emeryencyPlanTrees)
                {
                    isSuccess = emeryencyPlanTreeService.delete(emeryencyPlanTree.getId());
                }
            }
            Map<String, Object> resultMap = new HashMap<String, Object>();
            if (isSuccess) {
                resultMap.put("result", 1);
            } else {
                resultMap.put("result", 0);
            }

            resultMap.put("msg", Msg.DELETE.getMsg(isSuccess));
            return resultMap;
        }

    }

    /**
     * 后台编辑 删除树节点
     */
    @ResponseBody
    @RequestMapping("/deleteContent")
    public Map<String, Object> deleteContent(HttpServletRequest request,
                                             HttpSession session, Long id) throws Exception {
        boolean isSuccess = emeryencyPlanContentService.delete(id);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (isSuccess) {
            resultMap.put("result", 1);
        } else {
            resultMap.put("result", 0);
        }

        resultMap.put("msg", Msg.DELETE.getMsg(isSuccess));
        return resultMap;
    }
}

