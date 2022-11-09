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
	"github.com/robfig/cron/v3"
	"time"

	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/application/voice_chat/svc_service"
	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/pkg/config"
	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/pkg/logs"
	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/pkg/task"
)

var handler *EventHandler

type EventHandler struct {
	c *cron.Cron
}

func NewEventHandler() *EventHandler {
	if handler == nil {
		handler = &EventHandler{
			c: task.GetCronTask(),
		}
		handler.c.AddFunc("@every 1m", func() {
			ctx := context.Background()
			roomFactory := svc_service.GetRoomFactory()
			rooms, err := roomFactory.GetAllActiveRoomList(ctx, false)
			if err != nil {
				logs.CtxError(ctx, "cron: get svc rooms failed,error:%s", err)
				return
			}

			roomService := svc_service.GetRoomService()
			for _, room := range rooms {
				if time.Now().Sub(room.GetCreateTime()) >= time.Duration(config.Configs().SvcExperienceTime)*time.Minute {
					err = roomService.FinishLive(ctx, room.GetAppID(), room.GetRoomID(), svc_service.FinishTypeTimeout)
					if err != nil {
						logs.CtxError(ctx, "cron: finish room failed,error:%s", err)
						continue
					}
				}
			}

		})
	}
	return handler
}
