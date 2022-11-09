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
	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/pkg/config"

	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/application/voice_chat/svc_service"
	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/models/custom_error"
	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/models/public"
	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/pkg/logs"
)

type startLiveReq struct {
	UserID              string `json:"user_id"`
	UserName            string `json:"user_name"`
	RoomName            string `json:"room_name"`
	BackgroundImageName string `json:"background_image_name"`
	LoginToken          string `json:"login_token"`
}

type startLiveResp struct {
	RoomInfo *svc_service.Room `json:"room_info"`
	UserInfo *svc_service.User `json:"user_info"`
	RtcToken string            `json:"rtc_token"`
}

func (eh *EventHandler) StartLive(ctx context.Context, param *public.EventParam) (resp interface{}, err error) {
	logs.CtxInfo(ctx, "svcStartLive param:%+v", param)
	var p startLiveReq
	if err := json.Unmarshal([]byte(param.Content), &p); err != nil {
		logs.CtxWarn(ctx, "input format error, err: %v", err)
		return nil, custom_error.ErrInput
	}

	//校验参数
	if p.UserID == "" || p.UserName == "" || p.RoomName == "" {
		logs.CtxError(ctx, "input error, param:%v", p)
		return nil, custom_error.ErrInput
	}

	//todo 敏感词检测pkg

	//创建房间
	roomService := svc_service.GetRoomService()

	room, host, err := roomService.CreateRoom(ctx, param.AppID, p.RoomName, p.BackgroundImageName, p.UserID, p.UserName, param.DeviceID)
	if err != nil {
		logs.CtxError(ctx, "create room failed,error:%s", err)
		return nil, err
	}

	//开始直播
	err = roomService.StartLive(ctx, param.AppID, room.GetRoomID())
	if err != nil {
		logs.CtxError(ctx, "room start live failed,error:%s", err)
		return nil, err
	}

	resp = &startLiveResp{
		RoomInfo: room,
		UserInfo: host,
		RtcToken: room.GenerateToken(ctx, host.GetUserID(), config.Configs().SvcAppID, config.Configs().SvcAppKey),
	}

	return resp, nil
}
