import { request } from '@@/plugin-request/request';
import ProTable from '@ant-design/pro-table';
import { useRef } from 'react';
import { history } from '@@/core/history';

const Patient = (props)=>{

  const actionRef = useRef()

  const columns = [

    {
      title:'姓名',
      dataIndex:'name'
    },
    {
      title:'病案号',
      dataIndex:'caseId'
    },
    {
      title: '操作',
      valueType: 'option',
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      render: (text, record, _, action) => [
        <a
          key="editable"
          onClick={() => {
            // action?.startEditable?.(record.id);
            history.push({
              pathname: 'view',
              query: {
                formCode: props.location.query.formCode,
                caseId:record.caseId
              },
            });
          }}
        >
          上报
        </a>,
      ],
    },

  ]

  return(

    <>
      <ProTable
        search={false}
        actionRef={actionRef}
        columns={columns}
        rowKey="id"
        request={
          async () => {
            const config = await request("/api/quality/patient/config",{
              method:"GET",
              params:{param:JSON.stringify({formCode:props.location.query.formCode})}
            })
            console.log(config)
            const patientUrl = config.remarks
            const result = await request(patientUrl, {
              method: "GET",
            });
            console.log(result)
            return {
              data: result.value,
              success: true,
            };
          }
        }
      />
    </>

  )

}

export default Patient
