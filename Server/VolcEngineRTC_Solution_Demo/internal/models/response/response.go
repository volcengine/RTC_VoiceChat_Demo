/*
 * Copyright 2022 CloudWeGo Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package response

import (
	"context"
	"encoding/json"
	"time"

	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/models/custom_error"

	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/pkg/logs"
)

type CommonResponse struct {
	MessageType string      `json:"message_type"`
	Code        int         `json:"code"`
	RequestID   string      `json:"request_id"`
	Message     string      `json:"message"`
	Timestamp   int64       `json:"timestamp"`
	Response    interface{} `json:"response"`
}

func newCommonResponse(code int, message, requestID string, response interface{}) string {
	c := &CommonResponse{
		MessageType: "return",
		RequestID:   requestID,
		Code:        code,
		Message:     message,
		Timestamp:   time.Now().UnixNano(),
		Response:    response,
	}
	resByte, err := json.Marshal(c)
	if err != nil {
		logs.Warn("json marshal error, err: %v", err)
	}
	return string(resByte)
}

func NewCommonResponse(ctx context.Context, requestID string, response interface{}, err error) string {
	if err == nil {
		return newCommonResponse(200, "ok", requestID, response)
	}

	if cerr, ok := err.(*custom_error.CustomError); ok {
		return newCommonResponse(cerr.Code(), cerr.Error(), requestID, nil)
	}

	logs.CtxError(ctx, "new response failed,requestID:%s,response:%#v,err:%s", requestID, response, err)
	return ""
}
