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
package login_handler

import (
	"context"
	"encoding/json"
	"time"

	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/application/login/login_service"

	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/models/custom_error"
	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/models/public"

	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/pkg/logs"
)

const (
	PasswordFreeUserIDPrefix = "8848"
)

type passwordFreeLoginReq struct {
	UserName string `json:"user_name"`
}

type passwordFreeLoginResp struct {
	UserID     string `json:"user_id"`
	UserName   string `json:"user_name"`
	LoginToken string `json:"login_token"`
	CreatedAt  int64  `json:"created_at"`
}

func (h *EventHandler) PasswordFreeLogin(ctx context.Context, param *public.EventParam) (resp interface{}, err error) {
	var p passwordFreeLoginReq
	if err := json.Unmarshal([]byte(param.Content), &p); err != nil {
		logs.CtxWarn(ctx, "input format error, err: %v", err)
		return nil, custom_error.ErrInput
	}

	if p.UserName == "" {
		logs.CtxWarn(ctx, "input format error, params: %v", p)
		return nil, custom_error.ErrInput
	}

	userService := login_service.GetUserService()

	userID, err := userService.GenerateLocalUserIDWithRetry(ctx)
	if err != nil {
		logs.CtxError(ctx, "failed to generate userID, err: %v", err)
		return nil, err
	}
	userID = PasswordFreeUserIDPrefix + userID
	createdTime := time.Now().UnixNano()
	token := userService.GenerateLocalLoginToken(ctx, userID, createdTime)

	err = userService.Login(ctx, userID, token)
	if err != nil {
		logs.CtxError(ctx, "failed to login ,err: %v", err)
		return nil, err
	}
	err = userService.SetUserName(ctx, userID, p.UserName)
	if err != nil {
		logs.CtxError(ctx, "failed to set user name , err:%v", err)
		return nil, err
	}

	resp = &passwordFreeLoginResp{
		UserID:     userID,
		UserName:   p.UserName,
		LoginToken: token,
		CreatedAt:  createdTime,
	}

	return resp, nil
}
