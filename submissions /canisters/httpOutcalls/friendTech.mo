import Debug "mo:base/Debug";
import Blob "mo:base/Blob";
import Cycles "mo:base/ExperimentalCycles";
import Error "mo:base/Error";
import Array "mo:base/Array";
import Nat8 "mo:base/Nat8";
import Nat64 "mo:base/Nat64";
import Text "mo:base/Text";
import Types "Types";

actor {

  public query func transform(raw : Types.TransformArgs) : async Types.CanisterHttpResponsePayload {
      let transformed : Types.CanisterHttpResponsePayload = {
          status = raw.response.status;
          body = raw.response.body;
          headers = [
              {
                  name = "Content-Security-Policy";
                  value = "default-src 'self'";
              },
              { name = "Referrer-Policy"; value = "strict-origin" },
              { name = "Permissions-Policy"; value = "geolocation=(self)" },
              {
                  name = "Strict-Transport-Security";
                  value = "max-age=63072000";
              },
              { name = "X-Frame-Options"; value = "DENY" },
              { name = "X-Content-Type-Options"; value = "nosniff" },
          ];
      };
      transformed;
  };
  
  public func queryFriendTech(address: Text) : async Text {

    let ic : Types.IC = actor ("aaaaa-aa");
    // Friend Tech 정보 쿼리
    let url = "https://prod-api.kosetto.com/users/" # address;
    // 홀딩 정보
    // let url = "https://prod-api.kosetto.com/holdings-activity/" # address;

    let request_headers = [
        { name = "Content-Type"; value = "application/json" },
        { name = "Accept"; value = "application/json" },
    ];

    let transform_context : Types.TransformContext = {
      function = transform;
      context = Blob.fromArray([]);
    };

    let http_request : Types.HttpRequestArgs = {
        url = url;
        max_response_bytes = null;
        headers = request_headers;
        body = null;
        method = #get;
        transform = ?transform_context;
    };

    Cycles.add(50_000_000_000);
    
    let http_response : Types.HttpResponsePayload = await ic.http_request(http_request);
    let response_body: Blob = Blob.fromArray(http_response.body);
    let decoded_text: Text = switch (Text.decodeUtf8(response_body)) {
        case (null) { "No value returned" };
        case (?y) { y };
    };
    decoded_text
  };

  public func queryHolder(address: Text) : async Text {

    let ic : Types.IC = actor ("aaaaa-aa");
    // Friend Tech 홀딩 정보 쿼리
    let url = "https://friend-tech.p.rapidapi.com/users/" # address # "/token/holders";

    let request_headers = [
        { name = "X-RapidAPI-Key"; value = "bb15b5f23cmsh3f751f445c9fa10p11509ajsn8651c94b9d90" },
        { name = "X-RapidAPI-Host"; value = "friend-tech.p.rapidapi.com" },
    ];

    let transform_context : Types.TransformContext = {
      function = transform;
      context = Blob.fromArray([]);
    };

    let http_request : Types.HttpRequestArgs = {
        url = url;
        max_response_bytes = null;
        headers = request_headers;
        body = null;
        method = #get;
        transform = ?transform_context;
    };

    Cycles.add(50_000_000_000);
    
    let http_response : Types.HttpResponsePayload = await ic.http_request(http_request);
    let response_body: Blob = Blob.fromArray(http_response.body);
    let decoded_text: Text = switch (Text.decodeUtf8(response_body)) {
        case (null) { "No value returned" };
        case (?y) { y };
    };
    decoded_text
  };

};