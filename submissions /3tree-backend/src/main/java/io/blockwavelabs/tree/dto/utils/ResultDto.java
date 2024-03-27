package io.blockwavelabs.tree.dto.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@Builder
//@ApiModel -> swagger 관련 어노테이션
public class ResultDto<T> {
//    @ApiModelProperty(value = "결과 코드") -> swagger 관련 어노테이션
    private HttpStatus statusCode;
//    @ApiModelProperty(value = "결과 메시지") -> swagger 관련 어노테이션
    private String resultMsg;
//    @ApiModelProperty(value = "결과 데이터") -> swagger 관련 어노테이션
    private T resultData;

    public ResultDto(final HttpStatus statusCode, final String resultMsg) {
        this.statusCode = statusCode;
        this.resultMsg = resultMsg;
        this.resultData = null;
    }

    public static<T> ResultDto<T> res(final HttpStatus statusCode, final String resultMsg) {
        return res(statusCode, resultMsg, null);
    }

    public static<T> ResultDto<T> res(final HttpStatus statusCode, final String resultMsg, final T t) {
        return ResultDto.<T>builder()
                .resultData(t)
                .statusCode(statusCode)
                .resultMsg(resultMsg)
                .build();
    }
}
