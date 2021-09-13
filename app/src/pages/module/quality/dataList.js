import { request } from '@@/plugin-request/request';
import ProTable from '@ant-design/pro-table';
import { useRef } from 'react';
import { history } from '@@/core/history';

const DataList = ()=>{

  const actionRef = useRef()

  const columns = [

    {
      title:'类型',
      dataIndex:'type'
    },
    {
      title:'病案号',
      dataIndex:'caseId'
    },
    {
      title:'创建时间',
      dataIndex:'createDate',
      valueType:'dateTime'
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
                formCode: record.type,
                id:record.id
              },
            });
          }}
        >
          编辑
        </a>,
        <a
          onClick={async () => {
            const param = {
              id: record.id,
            }
            await request("/api/quality/view", {
              method: "DELETE",
              params: { param: JSON.stringify(param) },
            });
            actionRef.current.reload()
          }}
        >
          删除
        </a>,
      ],
    },

  ]

  return(
    <>
      <ProTable
        actionRef={actionRef}
        columns={columns}
        rowKey="id"
        request={
          async () => {
            const result = await request("/api/quality/view/list", {
              method: "GET",
            });
            return {
              data: result,
              success: true,
            };
          }
        }
      />
    </>
  )
}

export default DataList
