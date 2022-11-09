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

	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/application/login/login_service"

	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/models/custom_error"
	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/models/public"

	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/pkg/config"
	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/pkg/logs"
	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/pkg/token"
)

type joinRtmReq struct {
	LoginToken string `json:"login_token"`
	ScenesName string `json:"scenes_name"`
}

type joinRtmResp struct {
	AppID           string `json:"app_id"`
	RtmToken        string `json:"rtm_token"`
	ServerUrl       string `json:"server_url"`
	ServerSignature string `json:"server_signature"`
}

func (h *EventHandler) JoinRtm(ctx context.Context, param *public.EventParam) (resp interface{}, err error) {
	var p joinRtmReq
	if err := json.Unmarshal([]byte(param.Content), &p); err != nil {
		logs.CtxWarn(ctx, "input format error, err: %v", err)
		return nil, custom_error.ErrInput
	}

	userService := login_service.GetUserService()

	err = userService.CheckLoginToken(ctx, p.LoginToken)
	if err != nil {
		logs.CtxWarn(ctx, "login token expiry")
		return nil, err
	}
	//todo 根据场景选appID、appKey

	userID := userService.GetUserID(ctx, p.LoginToken)

	rtcToken, err := token.GenerateToken(&token.GenerateParam{
		AppID:        config.Configs().CsAppID,
		AppKey:       config.Configs().CsAppKey,
		RoomID:       "",
		UserID:       userID,
		ExpireAt:     7 * 24 * 3600,
		CanPublish:   true,
		CanSubscribe: true,
	})
	if err != nil {
		logs.CtxError(ctx, "generate token failed,error:%s", err)
		return nil, custom_error.InternalError(err)
	}

	resp = &joinRtmResp{
		AppID:           config.Configs().CsAppID,
		RtmToken:        rtcToken,
		ServerUrl:       config.Configs().ServerUrl,
		ServerSignature: p.LoginToken,
	}
	return resp, nil

}
