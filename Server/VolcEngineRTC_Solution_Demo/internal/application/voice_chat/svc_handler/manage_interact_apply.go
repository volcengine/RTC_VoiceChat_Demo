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

type manageInteractApplyReq struct {
	RoomID     string `json:"room_id"`
	UserID     string `json:"user_id"`
	Type       int    `json:"type"`
	LoginToken string `json:"login_token"`
}

type manageInteractApplyResp struct {
}

func (eh *EventHandler) ManageInteractApply(ctx context.Context, param *public.EventParam) (resp interface{}, err error) {
	logs.CtxInfo(ctx, "liveCreateLive param:%+v", param)
	var p manageInteractApplyReq
	if err := json.Unmarshal([]byte(param.Content), &p); err != nil {
		logs.CtxWarn(ctx, "input format error, err: %v", err)
		return nil, custom_error.ErrInput
	}

	//校验参数
	if p.RoomID == "" || p.UserID == "" {
		logs.CtxError(ctx, "input error, param:%v", p)
		return nil, custom_error.ErrInput
	}

	roomFactory := svc_service.GetRoomFactory()
	room, err := roomFactory.GetRoomByRoomID(ctx, param.AppID, p.RoomID)
	if err != nil {
		logs.CtxError(ctx, "get room failed,error:%s", err)
		return nil, err
	}
	if room == nil {
		logs.CtxError(ctx, "room is not exist ")
		return nil, custom_error.ErrRoomNotExist
	}
	if room.GetHostUserID() != p.UserID {
		logs.CtxError(ctx, "user is not host of room")
		return nil, custom_error.ErrUserIsNotOwner
	}

	if p.Type == 1 {
		room.OpenApply()
	} else {
		room.CloseApply()
	}
	err = roomFactory.Save(ctx, room)
	if err != nil {
		logs.CtxError(ctx, "get room factory failed,error:%s", err)
		return nil, err
	}

	return nil, nil
}
