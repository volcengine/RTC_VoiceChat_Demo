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
package svc_handler

import (
	"context"
	"encoding/json"

	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/models/public"

	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/application/voice_chat/svc_service"

	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/models/custom_error"
	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/pkg/logs"
)

type clearUserReq struct {
	UserID     string `json:"user_id"`
	LoginToken string `json:"login_token"`
}

type clearUserResp struct {
}

func (eh *EventHandler) ClearUser(ctx context.Context, param *public.EventParam) (resp interface{}, err error) {
	logs.CtxInfo(ctx, "svcClearUser param:%+v", param)
	var p clearUserReq
	if err := json.Unmarshal([]byte(param.Content), &p); err != nil {
		logs.CtxWarn(ctx, "input format error, err: %v", err)
		return nil, custom_error.ErrInput
	}

	userFactory := svc_service.GetUserFactory()
	user, err := userFactory.GetActiveUserByUserID(ctx, param.AppID, p.UserID)
	if err != nil {
		logs.CtxError(ctx, "get user failed,error:%s", err)
		return nil, err
	}
	if user == nil {
		return nil, nil
	}

	roomService := svc_service.GetRoomService()
	if user.IsHost() {
		err = roomService.FinishLive(ctx, param.AppID, user.GetRoomID(), svc_service.FinishTypeNormal)
		if err != nil {
			logs.CtxError(ctx, "finish live failed,error:%s", err)
			return nil, err
		}
	} else if user.IsAudience() {
		err = roomService.LeaveRoom(ctx, param.AppID, user.GetRoomID(), user.GetUserID())
		if err != nil {
			logs.CtxError(ctx, "leave room failed,error:%s", err)
			return nil, err
		}
	}

	//强制清除
	user, err = userFactory.GetActiveUserByUserID(ctx, param.AppID, p.UserID)
	if user != nil {
		user.LeaveRoom()
		userFactory.Save(ctx, user)
	}

	return nil, nil
}
