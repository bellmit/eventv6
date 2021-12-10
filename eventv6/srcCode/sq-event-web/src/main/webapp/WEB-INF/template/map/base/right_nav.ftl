
<div class="right-tital right-tital-boxshow1 ">
    <div class="search">
        <a href="javascript:void(0);" class="search-box flex flex-ac flex-jc">
            <img src="${rc.getContextPath()}/map/base/images/icon-search.png"/>
        </a>
    </div>
    <!--右边图层类型列表-->
    <ul id="menuList">
    </ul>
</div>
<div class="right-content rc-local-search" style="z-index: 1;">
    <a href="javascript:void(0);" class="right-content-switch right-content-switch1 flex flex-jc flex-ac">
        <img src="${rc.getContextPath()}/map/base/images/icon-to-right.png"/>
    </a>
    <div class="search-row flex">
        <div class="drop-down-box">
            <div class="dd-content">
                <p>全部</p>
                <img src="${rc.getContextPath()}/map/base/images/icon-down.png"/>
            </div>
            <div class="drop-down clearfix">
                <div class="dd-options">
                    <ul id="ddOptions">
                        <li><a href="javascript:void(0);">全部</a></li>
                        <#--<li><a href="javascript:void(0);">人</a></li>-->
                        <#--<li><a href="javascript:void(0);">地</a></li>-->
                        <#--<li><a href="javascript:void(0);">事</a></li>-->
                        <#--<li><a href="javascript:void(0);">物</a></li>-->
                        <#--<li><a href="javascript:void(0);">组织</a></li>-->
                    </ul>
                </div>
            </div>
        </div>
        <input type="text" class="search-input1 flex1" placeholder="请输入搜索关键词"/>

    </div>
    <div class="level flex flex-ac ">
        <span>搜索结果</span>
        <a href="#" class="close">
            <img src="${rc.getContextPath()}/map/base/images/icon_close.png"/>
        </a>
    </div>
    <!--没有搜索结果添加 类 no-content-->
    <div class="right-content-second right-content-second1">
        <i>
            <img src="${rc.getContextPath()}/map/base/images/icon-no-content.png"/>
            <p>暂无内容</p>
        </i>
        <div class="rc-content rc-content1 ">
            <div class="prompt mt10">
                <span>共查询到 <span class="color-red">3</span> 条记录</span>
            </div>
            <div class="rc-content-box rc-content-box1">
                <a href="#" class="rc-content-items  flex flex-clm">
                    <div class="rcc-items-tital flex">
                        <img src="${rc.getContextPath()}/map/base/images/icon-event.png"/>
                        <p>事件名称事件名称事件名称事件名称事件名称</p>
                        <i class="bg-blue">待处理</i>
                    </div>
                    <div class="rcc-items-row flex mt10">
                        <img src="${rc.getContextPath()}/map/base/images/icon_address.png"/>
                        <p class="flex1">福州市晋安区福新东路10号11室</p>
                    </div>
                    <div class="rcc-items-row flex mt10">
                        <img src="${rc.getContextPath()}/map/base/images/icon-time.png"/>
                        <p class="flex1">2018-09-01 12:20:30</p>
                    </div>
                    <i class="linkage">
                        联动
                    </i>
                </a>

                <a href="#" class="rc-content-items  flex flex-clm">
                    <div class="rcc-items-tital flex">
                        <img src="${rc.getContextPath()}/map/base/images/icon_user_woman.png">
                        <p class="flex1">张三</p>
                    </div>
                    <div class="rcc-items-row flex mt10">
                        <img src="${rc.getContextPath()}/map/base/images/icon_address.png">
                        <p class="flex1">福州市晋安区福新东路10号11室</p>
                    </div>
                    <div class="rcc-items-row flex mt10">
                        <img src="${rc.getContextPath()}/map/base/images/icon_phone.png">
                        <p class="flex1">15600000000</p>
                    </div>
                    <div class="rcc-items-row flex mt10">
                        <img src="${rc.getContextPath()}/map/base/images/icon_id_card.png">
                        <p class="flex1">350128196901111322</p>
                    </div>
                </a>

                <a href="#" class="rc-content-items  flex flex-clm">
                    <div class="rcc-items-tital flex">
                        <img src="${rc.getContextPath()}/map/base/images/icon-name.png"/>
                        <p class="flex1">名称</p>
                    </div>
                    <div class="rcc-items-row flex mt10">
                        <img src="${rc.getContextPath()}/map/base/images/icon_address.png"/>
                        <p class="flex1">福州市晋安区福新东路10号11室</p>
                    </div>
                    <div class="rcc-items-row flex mt10">
                        <img src="${rc.getContextPath()}/map/base/images/icon-people.png"/>
                        <p class="flex1">张三</p>
                    </div>
                    <div class="rcc-items-row flex mt10">
                        <img src="${rc.getContextPath()}/map/base/images/icon_phone.png"/>
                        <p class="flex1">15600000000</p>
                    </div>
                </a>

                <a href="#" class="rc-content-items  flex flex-clm">
                    <div class="rcc-items-tital flex">
                        <img src="${rc.getContextPath()}/map/base/images/icon-things.png"/>
                        <p class="flex1 ">物名称</p>
                    </div>
                    <div class="rcc-items-row flex mt10">
                        <img src="${rc.getContextPath()}/map/base/images/icon_address.png"/>
                        <p class="flex1">福州市晋安区福新东路10号11室</p>
                    </div>
                </a>

                <a href="#" class="rc-content-items  flex flex-clm">
                    <div class="rcc-items-tital flex">
                        <img src="${rc.getContextPath()}/map/base/images/icon-organization.png"/>
                        <p class="flex1">组织名称</p>
                    </div>
                    <div class="rcc-items-row flex mt10">
                        <img src="${rc.getContextPath()}/map/base/images/icon_address.png"/>
                        <p class="flex1">福州市晋安区福新东路10号11室</p>
                    </div>
                    <div class="rcc-items-row flex mt10">
                        <img src="${rc.getContextPath()}/map/base/images/icon-people.png"/>
                        <p class="flex1">张三</p>
                    </div>
                    <div class="rcc-items-row flex mt10">
                        <img src="${rc.getContextPath()}/map/base/images/icon_phone.png"/>
                        <p class="flex1">15600000000</p>
                    </div>
                </a>

                <a href="#" class="rc-content-items  flex flex-clm">
                    <div class="rcc-items-tital flex">
                        <img src="${rc.getContextPath()}/map/base/images/icon-event.png"/>
                        <p>事件名称</p>
                        <i class="bg-green">已结案</i>
                    </div>
                    <div class="rcc-items-row flex mt10">
                        <img src="${rc.getContextPath()}/map/base/images/icon_address.png"/>
                        <p class="flex1">福州市晋安区福新东路10号11室</p>
                    </div>
                    <div class="rcc-items-row flex mt10">
                        <img src="${rc.getContextPath()}/map/base/images/icon-time.png"/>
                        <p class="flex1">2018-09-01 12:20:30</p>
                    </div>
                    <i class="linkage">
                        联动
                    </i>
                </a>
            </div>
            <div class="rc-bottom ">
                <div class="rc-bottom-row">
                    <span class="fl ml5">共</span>
                    <input type="text" class="page fl" value="1"/>
                    <span class="fl">页/296329页</span>
                    <a href="#" class="fr mr15">
                        <img src="${rc.getContextPath()}/map/base/images/right_icon.png"/>
                    </a>
                    <!--当不可点击是添加类 active1-->
                    <a href="#" class="fr active1 mr5">
                        <img src="${rc.getContextPath()}/map/base/images/right_icon.png"
                             class="rotate180"/>
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>



<div class="right-content rc-local-search-option" style="display: none;z-index: 1;">
    <a href="javascript:void(0);" class="right-content-switch right-content-switch1 flex flex-jc flex-ac">
        <img src="${rc.getContextPath()}/map/base/images/icon-to-right.png"/>
    </a>
    <div class="level flex flex-ac ">
        <span>当前位置：</span>
        <div class="first">
            <a href="#" class="back">人</a>
        </div>
        <div class="second">
            <img src="${rc.getContextPath()}/map/base/images/right_icon.png"
                 class=" fl"/>
            <span class="fl ml8">党员</span>
        </div>
        <a href="#" class="close">
            <img src="${rc.getContextPath()}/map/base/images/icon_close.png"/>
        </a>
    </div>
    <div class="right-content-first  flex flex-clm">
        <div class="level-content flex1">
            <ul class="flex flex-wrap mb15" id="childOptions">
                <!--子图层列表-->
                <#--<li>-->
                    <#--<a href="#" class="flex flex-ac flex-jc mt10">-->
                        <#--<span>党员</span>-->
                    <#--</a>-->
                <#--</li>-->
            </ul>
        </div>
    </div>
    <!--没有搜索结果添加 类 no-content-->
    <div class="right-content-second ">
        <input type="text" class="search-input" placeholder="请输入搜索关键词"/>
        <i>
            <img src="${rc.getContextPath()}/map/base/images/icon-no-content.png"/>
            <p>暂无内容</p>
        </i>
        <div class="rc-content ">
            <div class="prompt mt10">
                <span>共查询到 <span class="color-red">3</span> 条记录</span>
            </div>
            <div class="rc-content-box">
                <a href="#" class="rc-content-items  flex flex-clm">
                    <div class="rcc-items-tital flex">
                        <img src="${rc.getContextPath()}/map/base/images/icon_user_woman.png">
                        <p class="flex1">张三</p>
                    </div>
                    <div class="rcc-items-row flex mt10">
                        <img src="${rc.getContextPath()}/map/base/images/icon_address.png">
                        <p class="flex1">福州市晋安区福新东路10号11室</p>
                    </div>
                    <div class="rcc-items-row flex mt10">
                        <img src="${rc.getContextPath()}/map/base/images/icon_phone.png">
                        <p class="flex1">15600000000</p>
                    </div>
                    <div class="rcc-items-row flex mt10">
                        <img src="${rc.getContextPath()}/map/base/images/icon_id_card.png">
                        <p class="flex1">350128196901111322</p>
                    </div>
                </a>

                <a href="#" class="rc-content-items  flex flex-clm">
                    <div class="rcc-items-tital flex">
                        <img src="${rc.getContextPath()}/map/base/images/icon_user_woman.png">
                        <p class="flex1">张三</p>
                    </div>
                    <div class="rcc-items-row flex mt10">
                        <img src="${rc.getContextPath()}/map/base/images/icon_address.png">
                        <p class="flex1">福州市晋安区福新东路10号11室</p>
                    </div>
                    <div class="rcc-items-row flex mt10">
                        <img src="${rc.getContextPath()}/map/base/images/icon_phone.png">
                        <p class="flex1">15600000000</p>
                    </div>
                    <div class="rcc-items-row flex mt10">
                        <img src="${rc.getContextPath()}/map/base/images/icon_id_card.png">
                        <p class="flex1">350128196901111322</p>
                    </div>
                </a>

                <a href="#" class="rc-content-items  flex flex-clm">
                    <div class="rcc-items-tital flex">
                        <img src="${rc.getContextPath()}/map/base/images/icon_user_woman.png">
                        <p class="flex1">张三</p>
                    </div>
                    <div class="rcc-items-row flex mt10">
                        <img src="${rc.getContextPath()}/map/base/images/icon_address.png">
                        <p class="flex1">福州市晋安区福新东路10号11室</p>
                    </div>
                    <div class="rcc-items-row flex mt10">
                        <img src="${rc.getContextPath()}/map/base/images/icon_phone.png">
                        <p class="flex1">15600000000</p>
                    </div>
                    <div class="rcc-items-row flex mt10">
                        <img src="${rc.getContextPath()}/map/base/images/icon_id_card.png">
                        <p class="flex1">350128196901111322</p>
                    </div>
                </a>

                <a href="#" class="rc-content-items  flex flex-clm">
                    <div class="rcc-items-tital flex">
                        <img src="${rc.getContextPath()}/map/base/images/icon_user_woman.png">
                        <p class="flex1">张三</p>
                    </div>
                    <div class="rcc-items-row flex mt10">
                        <img src="${rc.getContextPath()}/map/base/images/icon_address.png">
                        <p class="flex1">福州市晋安区福新东路10号11室</p>
                    </div>
                    <div class="rcc-items-row flex mt10">
                        <img src="${rc.getContextPath()}/map/base/images/icon_phone.png">
                        <p class="flex1">15600000000</p>
                    </div>
                    <div class="rcc-items-row flex mt10">
                        <img src="${rc.getContextPath()}/map/base/images/icon_id_card.png">
                        <p class="flex1">350128196901111322</p>
                    </div>
                </a>

                <a href="#" class="rc-content-items  flex flex-clm">
                    <div class="rcc-items-tital flex">
                        <img src="${rc.getContextPath()}/map/base/images/icon_user_woman.png">
                        <p class="flex1">张三</p>
                    </div>
                    <div class="rcc-items-row flex mt10">
                        <img src="${rc.getContextPath()}/map/base/images/icon_address.png">
                        <p class="flex1">福州市晋安区福新东路10号11室</p>
                    </div>
                    <div class="rcc-items-row flex mt10">
                        <img src="${rc.getContextPath()}/map/base/images/icon_phone.png">
                        <p class="flex1">15600000000</p>
                    </div>
                    <div class="rcc-items-row flex mt10">
                        <img src="${rc.getContextPath()}/map/base/images/icon_id_card.png">
                        <p class="flex1">350128196901111322</p>
                    </div>
                </a>

                <a href="#" class="rc-content-items  flex flex-clm">
                    <div class="rcc-items-tital flex">
                        <img src="${rc.getContextPath()}/map/base/images/icon_user_woman.png">
                        <p class="flex1">张三</p>
                    </div>
                    <div class="rcc-items-row flex mt10">
                        <img src="${rc.getContextPath()}/map/base/images/icon_address.png">
                        <p class="flex1">福州市晋安区福新东路10号11室</p>
                    </div>
                    <div class="rcc-items-row flex mt10">
                        <img src="${rc.getContextPath()}/map/base/images/icon_phone.png">
                        <p class="flex1">15600000000</p>
                    </div>
                    <div class="rcc-items-row flex mt10">
                        <img src="${rc.getContextPath()}/map/base/images/icon_id_card.png">
                        <p class="flex1">350128196901111322</p>
                    </div>
                </a>
            </div>
            <div class="rc-bottom ">
                <div class="rc-bottom-row">
                    <span class="fl ml5">共</span>
                    <input type="text" class="page fl" value="1"/>
                    <span class="fl">页/296329页</span>
                    <a href="#" class="fr mr15">
                        <img src="${rc.getContextPath()}/map/base/images/right_icon.png"/>
                    </a>
                    <!--当不可点击是添加类 active1-->
                    <a href="#" class="fr active1 mr5">
                        <img src="${rc.getContextPath()}/map/base/images/right_icon.png"
                             class="rotate180"/>
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>


