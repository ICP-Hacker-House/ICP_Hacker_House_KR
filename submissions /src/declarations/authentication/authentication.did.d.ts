import type { Principal } from '@dfinity/principal';
import type { ActorMethod } from '@dfinity/agent';

export interface _SERVICE {
  'query_ethAddress' : ActorMethod<[string], string>,
  'query_secretHash' : ActorMethod<[string], string>,
  'query_secretProvided' : ActorMethod<[string], boolean>,
  'status_initialize' : ActorMethod<[string], boolean>,
  'update_ethAddress' : ActorMethod<[string, string], boolean>,
  'update_secretHash' : ActorMethod<[string, string], boolean>,
  'update_secretProvided' : ActorMethod<[string, boolean], boolean>,
}
