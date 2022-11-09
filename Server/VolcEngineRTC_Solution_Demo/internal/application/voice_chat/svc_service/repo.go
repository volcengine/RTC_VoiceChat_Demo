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
package svc_service

import (
	"context"

	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/application/voice_chat/svc_db"
	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/application/voice_chat/svc_entity"
	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/application/voice_chat/svc_redis"
)

var roomRepoClient RoomRepo
var userRepoClient UserRepo
var seatRepoClient SeatRepo

func GetRoomRepo() RoomRepo {
	if roomRepoClient == nil {
		roomRepoClient = &svc_db.DbRoomRepo{}
	}
	return roomRepoClient
}

func GetUserRepo() UserRepo {
	if userRepoClient == nil {
		userRepoClient = &svc_db.DbUserRepo{}
	}
	return userRepoClient
}

func GetSeatRepo() SeatRepo {
	if seatRepoClient == nil {
		seatRepoClient = &svc_redis.RedisSeatRepo{}
	}
	return seatRepoClient
}

type RoomRepo interface {
	//create or update
	Save(ctx context.Context, room *svc_entity.SvcRoom) error

	//get
	GetRoomByRoomID(ctx context.Context, appID, roomID string) (*svc_entity.SvcRoom, error)
	GetActiveRooms(ctx context.Context, appID string) ([]*svc_entity.SvcRoom, error)
	GetAllActiveRooms(ctx context.Context) ([]*svc_entity.SvcRoom, error)
}

type UserRepo interface {
	//create or update
	Save(ctx context.Context, user *svc_entity.SvcUser) error

	//update users
	UpdateUsersWithMapByRoomID(ctx context.Context, appID, roomID string, ups map[string]interface{}) error

	//get user
	GetActiveUserByRoomIDUserID(ctx context.Context, appID, roomID, userID string) (*svc_entity.SvcUser, error)
	GetUserByRoomIDUserID(ctx context.Context, appID, roomID, userID string) (*svc_entity.SvcUser, error)
	GetActiveUserByUserID(ctx context.Context, appID, userID string) (*svc_entity.SvcUser, error)

	//get users
	GetActiveUsersByRoomID(ctx context.Context, appID, roomID string) ([]*svc_entity.SvcUser, error)
	GetAudiencesWithoutApplyByRoomID(ctx context.Context, appID, roomID string) ([]*svc_entity.SvcUser, error)
	GetApplyAudiencesByRoomID(ctx context.Context, appID, roomID string) ([]*svc_entity.SvcUser, error)
}

type SeatRepo interface {
	//create or update
	Save(ctx context.Context, seat *svc_entity.SvcSeat) error

	//get
	GetSeatByRoomIDSeatID(ctx context.Context, roomID string, seatID int) (*svc_entity.SvcSeat, error)
	GetSeatsByRoomID(ctx context.Context, roomID string) ([]*svc_entity.SvcSeat, error)
}
