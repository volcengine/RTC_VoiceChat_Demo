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
package inform

import (
	"context"

	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/models/response"
	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/pkg/logs"
	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/pkg/rtc_openapi"
)

type InformEvent string

type InformService struct {
	AppID string
}

var informServiceMap map[string]*InformService

func GetInformService(appID string) *InformService {
	if informServiceMap == nil {
		informServiceMap = make(map[string]*InformService, 10)
	}
	informService, ok := informServiceMap[appID]
	if ok {
		return informService
	} else {
		informService = &InformService{
			AppID: appID,
		}
		informServiceMap[appID] = informService
	}

	return informService
}

func (is *InformService) BroadcastRoom(ctx context.Context, roomID string, event InformEvent, data interface{}) {
	instance := rtc_openapi.GetInstance()
	err := instance.RtmSendBroadcast(ctx, is.AppID, roomID, response.NewInformToClient(string(event), data))
	if err != nil {
		logs.CtxError(ctx, "rtm send broad cast failed,event:%s,data:%#v,error:%s", event, data, err)
	}
}

func (is *InformService) UnicastUser(ctx context.Context, roomID, userID string, event InformEvent, data interface{}) {
	instance := rtc_openapi.GetInstance()
	err := instance.RtmSendRoomUnicast(ctx, is.AppID, roomID, userID, response.NewInformToClient(string(event), data))
	if err != nil {
		logs.CtxError(ctx, "rtm send broad cast failed,event:%s,data:%#v,error:%s", event, data, err)
	}
}
