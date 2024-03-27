import type { Principal } from '@dfinity/principal';
import type { ActorMethod } from '@dfinity/agent';

export interface Comment { 'commentor' : string, 'comment' : string }
export interface _SERVICE {
  'create_index' : ActorMethod<[string], boolean>,
  'create_profile' : ActorMethod<[string], boolean>,
  'query_comments' : ActorMethod<[string], Array<Comment>>,
  'query_index' : ActorMethod<[string], Array<string>>,
  'query_principleToProfile' : ActorMethod<[string], string>,
  'query_profile' : ActorMethod<[string], Array<string>>,
  'rate' : ActorMethod<[string, string, boolean, string], boolean>,
  'update_index' : ActorMethod<[string, string, bigint], boolean>,
  'update_profile' : ActorMethod<[string, string, bigint], boolean>,
}
