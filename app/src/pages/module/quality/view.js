import { useState } from 'react';
import ProForm,{ BetaSchemaForm } from '@ant-design/pro-form';
import { request } from '@@/plugin-request/request';
import { FooterToolbar } from '@ant-design/pro-layout';
import { history } from '@@/core/history';
import { message, notification } from 'antd';
import Form from 'antd/es/form/Form';

const View = (props)=>{

  const [dataList,setDataList] = useState([])
  const [columns,setColumns] = useState([])
  const [form] = Form.useForm();
  return(
    <>
      <ProForm
        form={form}
        request={
          async () => {
            const result = await request("/api/quality/view", {
              method: "GET",
              params: { "param": JSON.stringify(
                {
                  formCode:props.location.query.formCode,
                  id:props.location.query.id,
                  caseId:props.location.query.caseId
                }
                )
              },
            });
            let temp = []
            result.map((value, index, array)=>{
              const obj = {
                title: value.name,
                dataIndex:value.code,
                valueType: value.type,
                fieldProps:{
                  // addonBefore:value.name,
                  options:value.fieldProps.options,
                },
                formItemProps: {
                  name:value.code,
                  dependencies:value.dependencies,
                  rules: [
                    {

                      required: value.required==='y'?true:false,
                      message: '此项为必填项',
                    },
                  ],

                  labelCol:{span: 6},
                  initialValue:value.initData
                }
              };
              temp.push(obj)
            })
            setColumns(temp)
            setDataList(result)
            return result
          }

        }
        onFinish={async (values) => {

          // eslint-disable-next-line no-param-reassign
          values.formCode = props.location.query.formCode
          // eslint-disable-next-line no-param-reassign
          values.id = props.location.query.id
          const result =  await request("/api/quality/view", {
            method: "POST",
            data: { "param": JSON.stringify(values) },
            requestType: 'form'
          });
          if(result.code==="0"){
            message.success('提交成功');
          }else {
            notification.open({
              message: '提示',
              description:result.msg,
              duration:null
            });
          }
          history.push({
            pathname: 'data',
          });

        }}
        layout="horizontal"
        onFieldsChange={(changedFields, allFields)=>{
          const fieldName = changedFields[0].name[0];
          const fieldValue = changedFields[0].value;
          let flag = false;
          dataList.map((value)=>{
            if(fieldName===value.code){
              if(value.rules!==undefined){
                const rules = JSON.parse(value.rules)
                rules.map((rule)=>{
                  if(fieldValue.indexOf(rule.value)!==-1){
                    const targets = rule.target;
                    columns.map((column) => {
                              if(targets===column.dataIndex){
                                column.formItemProps.rules[0].required = true
                                flag = true;
                              }
                    })
                  }
                })
              }
            }
          })


          if(!flag){
            for(const i in dataList){
              if(fieldName===dataList[i].code){
                var rules = dataList[i].rules;
                if(rules!=undefined){
                  rules = JSON.parse(rules)
                  for(const i1 in rules){
                    var targets = rules[i1].target.split(",");
                    for (let j = 0; j < targets.length; j++) {
                      var targetFieldName = targets[j];
                      for (const targetColumn in columns) {
                        if(targetFieldName==columns[targetColumn].dataIndex){
                          columns[targetColumn].formItemProps.rules[0].required = false
                        }
                      }
                    }
                  }
                }
              }
            }
          }


        }}



        submitter={{
          render: (_, dom) => <FooterToolbar>{dom}</FooterToolbar>,
        }}

      >
        <BetaSchemaForm
          trigger={<a>点击我</a>}
          layoutType="Embed"
          columns={columns}

        />
      </ProForm>
    </>
  )
}

export default View
