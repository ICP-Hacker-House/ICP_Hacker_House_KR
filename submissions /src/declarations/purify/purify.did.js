export const idlFactory = ({ IDL }) => {
  const Comment = IDL.Record({ 'commentor' : IDL.Text, 'comment' : IDL.Text });
  return IDL.Service({
    'create_index' : IDL.Func([IDL.Text], [IDL.Bool], []),
    'create_profile' : IDL.Func([IDL.Text], [IDL.Bool], []),
    'query_comments' : IDL.Func([IDL.Text], [IDL.Vec(Comment)], []),
    'query_index' : IDL.Func([IDL.Text], [IDL.Vec(IDL.Text)], []),
    'query_principleToProfile' : IDL.Func([IDL.Text], [IDL.Text], []),
    'query_profile' : IDL.Func([IDL.Text], [IDL.Vec(IDL.Text)], []),
    'rate' : IDL.Func([IDL.Text, IDL.Text, IDL.Bool, IDL.Text], [IDL.Bool], []),
    'update_index' : IDL.Func([IDL.Text, IDL.Text, IDL.Nat], [IDL.Bool], []),
    'update_profile' : IDL.Func([IDL.Text, IDL.Text, IDL.Nat], [IDL.Bool], []),
  });
};
export const init = ({ IDL }) => { return []; };
