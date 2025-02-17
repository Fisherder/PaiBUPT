<template>
   <el-main height="">
    <el-form :model="searchParm" ref="form" :inline="true" size="default">
        <el-form-item>
            <el-input placeholder="请输入标题" v-model="searchParm.title"></el-input>
        </el-form-item>
        <el-form-item>
            <el-button icon="Search" @click="searchBtn">搜索</el-button>
            <el-button @click="resetBtn" type="danger"  plain icon="Close">重置</el-button>
            <el-button icon="Plus" type="primary" @click="addBtn">新增</el-button>
        </el-form-item>
    </el-form>
    <!-- 表格数据 -->
     <el-table :height="tableHeight" :data="tableList" border stripe>
        <el-table-column prop="title" label="标题"></el-table-column>
        <el-table-column prop="images" label="图片">
            <template #default="scope">
                 <el-image
                    v-if="scope.row.images"
                    style="width: 100px; height: 60px"
                    :src="scope.row.images.split(',')[0]"/>
            </template>
        </el-table-column>
        <el-table-column prop="status" label="状态">
            <!-- 0上架，1下架 -->
            <template #default="scope">
                <el-switch
                    v-model="scope.row.status"
                    :active-value="'0'"
                    :inactive-value="'1'"
                    :before-change="beforeStatus"
                    @change="changeStatus(scope.row.status, scope.row.banId)"/>
            </template>
        </el-table-column>
        <el-table-column prop="orderNum" label="序号"></el-table-column>
        <el-table-column label="操作" align="center" width="220">
            <template #default="scope">
                <el-button icon="Edit" type="primary" size="default" @click="editBtn(scope.row)">编辑</el-button>
                <el-button icon="Delete" type="danger" size="default" @click="deleteBtn(scope.row)">删除</el-button>
            </template>
        </el-table-column>
     </el-table>
     <!-- 分页 -->
     <el-pagination
        @size-change="sizeChange"
        @current-change="currentChange"
        :current-page.sync="searchParm.currentPage"
        :page-sizes="[10,20, 40, 80, 100]"
        :page-size="searchParm.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="searchParm.total" background>
</el-pagination>
    <!-- 弹框 -->
     <SysDialog
     :title="dialog.title"
     :visible="dialog.visible"
     :width="dialog.width"
    :height="dialog.height"
    @onClose="onClose"
    @onConfirm="commit"
     >
            <template v-slot:content>
                <el-form
                    :model="addModel"
                    ref="addRef"
                    :rules="rules"
                    label-width="80px"
                    :inline="false"
                    size="default"
                    style="width: 100%;"
                        >
                <el-form-item prop="title"label="标题">
                    <el-input v-model="addModel.title"></el-input>
                </el-form-item>
                <el-form-item prop="goodsId"label="商品">
                    <el-select
                        v-model="addModel.goodsId"
                        class="m-2"
                        placeholder="请选择商品"
                        size="default"
                        >
                        <el-option
                        v-for="item in selectList"
                        :key="item['value']"
                        :label="item['label']"
                        :value="item['value']"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item prop="orderNum"label="序号">
                    <el-input v-model="addModel.orderNum"></el-input>
                </el-form-item>
                <el-form-item prop="status" label="状态:">
                <el-radio-group v-model="addModel.status">
                <el-radio :label="'0'">启用</el-radio>
                <el-radio :label="'1'">停用</el-radio>
                </el-radio-group>
                </el-form-item>
                <el-form-item prop="images"label="图片">
                <UploadImage
                ref="upImgRef"
                @getImg="getImg"
                :oldUrl="oldUrl"
                :fileList="fileList"
                ></UploadImage>
                </el-form-item>
                </el-form>
            </template>
     </SysDialog>
   </el-main>
</template>

<script setup lang="ts">
import { nextTick, onMounted, reactive, ref } from 'vue';
import SysDialog from '../../components/SysDialog.vue';
import useDialog from '../../hooks/useDialog';
import UploadImage from "@/components/UploadImage.vue";
import { ElMessage, type FormInstance, type UploadUserFile } from 'element-plus'
import type { NewType } from '../../type/BaseType';
import { addCategoryApi, getGoodsListApi,getListApi,upanddownApi,editApi,deleteApi } from '../../api/banner';
import useInstance from '../../hooks/useInstance';
import { type Banner } from '../../api/banner/BannerModel';
import { EditType } from '../../type/BaseEnum';
const {global}=useInstance()
//表单ref属性
const addRef=ref<FormInstance>()
//图片上传的ref属性
const upImgRef = ref();
//图片初始值
const fileList = ref<Array<UploadUserFile>>([]);
//回显的图片
const oldUrl = ref<Array<{ url: string }>>([]);
//图片地址
const imgUrl = ref<Array<{ url: string }>>([]);
//获取弹框属性
const{dialog,onClose,onShow}=useDialog()
//搜索参数
const searchParm=reactive({
    title: "",
    currentPage:1,
    pageSize:10,
    total:0
});

//表单绑定的对象
const addModel = reactive({
banId: "",
goodsId: "",
title: "",
images: "",
status: "",
orderNum: "",
});
//表单验证规则
const rules = reactive({
title:[
{
required:true,
trigger:'change',
message:'请填写标题'
}
],
goodsId:[
{
required:true,
trigger:'change',
message:'请选择商品'
}
],
images:[
{
required:true,
trigger:'change',
message:'请上传图片'
}
],
status:[
{
required:true,
trigger:'change',
message:'请选择状态'
}
],
orderNum:[
{
required:true,
trigger:'change',
message:'请填写序号'
}
]
});
//接收图片上传地址
const getImg = (img: NewType) => {
console.log("999");
console.log(img);
imgUrl.value = oldUrl.value.concat(img.newImgUrl);
if (img.deleteUrl.length > 0) {
let newArr = imgUrl.value.filter(
(x) => !img.deleteUrl.some((item) => x.url === item.url)
);
imgUrl.value = newArr;
}
// console.log(imgUrl.value)
//把图片路径拼接为逗号分隔的字符串
let url = "";
for (let k = 0; k < imgUrl.value.length; k++) {
url = url + imgUrl.value[k].url + ",";
}
addModel.images = url.substring(0, url.lastIndexOf(","));
console.log(addModel.images);
};

//定义商品数据
const selectList=ref([])
//获取下拉商品列表
const getGoodsList=async()=>{
    let res=await getGoodsListApi()
    if(res && res.code==200){
        selectList.value=res.data
    }
}

//表格数据
const tableList=ref([])
//获取列表
const getList=async()=>{
    let res=await getListApi(searchParm)
    if(res && res.code==200){
        tableList.value=res.data.records;
        searchParm.total=res.data.total;
    }
}
//上架下架操作之前的事件
const beforeStatus = async () => {
        let confirm = await global.$myconfirm("确定上下架吗？");
        return new Promise((resolve, reject) => {
        if (confirm) {
            return resolve(confirm);
        } else {
            return reject(confirm);
    }
    });
    };
    //上下架操作
    const changeStatus = async (type: string, goodsId: string) => {
        console.log(type);
        console.log(goodsId);
        let res = await upanddownApi({
            banId: goodsId,
            status: type
    });
        if (res && res.code == 200) {
            getGoodsList();
    }
    };
//页容量改变时触发
const sizeChange = (size: number) => {
searchParm.pageSize = size;
getList();
};
//页数改变时触发
const currentChange = (page: number) => {
searchParm.currentPage = page;
getList();
};
//重置
const resetBtn = () => {
searchParm.title = "";
getList();
};
//搜索
const searchBtn = () => {
getList();
};
const tags=ref('')
//新增
const addBtn=()=>{
    //清空图片数据
    imgUrl.value=[]
    oldUrl.value=[]
    fileList.value=[]
    tags.value='0'
    dialog.width=700;
    dialog.height=400;
    //获取商品数据
    getGoodsList();
    onShow();

}
//编辑
const editBtn=(row:Banner)=>{
    tags.value='1'
     //清空图片数据
     imgUrl.value=[]
    oldUrl.value=[]
    fileList.value=[]
    dialog.width=700;
    dialog.height=400;
    //获取商品数据
    getGoodsList();
    console.log(row)
    onShow()
    //数据回显
    nextTick(()=>{
        Object.assign(addModel,row);
        //图片回显
        if(addModel.images){
            //逗号分割转数组
            let imgs=addModel.images.split(",");
            for(let i=0;i<imgs.length;i++){
                let img={name:'',url:''}
                img.name=imgs[i]
                img.url=imgs[i]
                fileList.value.push(img)
                oldUrl.value.push({url:imgs[i]})
            }
        }
    })
    //清空表单
    addRef.value?.resetFields()
}
//删除
//async表异步
const deleteBtn=async(row:Banner)=>{
    console.log(row)
    let confirm=await global.$myconfirm('确定删除该数据吗？');
    if(confirm){
        let res=await deleteApi(row)
        if(res && res.code==200){
            ElMessage.success(res.msg);
                getList();
        }
    }
}
//表单提交
const commit=()=>{
    addRef.value?.validate(async(valid)=>{
        if(valid){
            let res=null
            if(tags.value==EditType.ADD){
                res=await addCategoryApi(addModel)
            }
            else{
                res=await editApi(addModel)
            }
            if(res && res.code==200){
                ElMessage.success(res.msg);
                //清空图片
                upImgRef.value.clearimg()
                getList();
                onClose();
            }
        }
    })
}
//表格高度
const tableHeight=ref(0)
onMounted(()=>{
    getList()
    nextTick(()=>{
        tableHeight.value=window.innerHeight-220
    })
})
</script>

<style scoped>

</style>