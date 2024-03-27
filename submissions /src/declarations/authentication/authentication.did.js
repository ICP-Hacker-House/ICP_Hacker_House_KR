export const idlFactory = ({ IDL }) => {
  return IDL.Service({
    'query_ethAddress' : IDL.Func([IDL.Text], [IDL.Text], []),
    'query_secretHash' : IDL.Func([IDL.Text], [IDL.Text], []),
    'query_secretProvided' : IDL.Func([IDL.Text], [IDL.Bool], []),
    'status_initialize' : IDL.Func([IDL.Text], [IDL.Bool], []),
    'update_ethAddress' : IDL.Func([IDL.Text, IDL.Text], [IDL.Bool], []),
    'update_secretHash' : IDL.Func([IDL.Text, IDL.Text], [IDL.Bool], []),
    'update_secretProvided' : IDL.Func([IDL.Text, IDL.Bool], [IDL.Bool], []),
  });
};
export const init = ({ IDL }) => { return []; };
