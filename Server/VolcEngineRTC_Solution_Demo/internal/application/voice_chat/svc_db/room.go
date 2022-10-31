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
package svc_db

import (
	"context"

	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/pkg/util"

	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/application/voice_chat/svc_entity"
	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/pkg/db"
	"gorm.io/gorm"
	"gorm.io/gorm/clause"
)

const RoomTable = "svc_room"

const (
	RoomStatusPrepare = 1
	RoomStatusStart   = 2
	RoomStatusFinish  = 3
)

type DbRoomRepo struct{}

func (repo *DbRoomRepo) Save(ctx context.Context, liveRoom *svc_entity.SvcRoom) error {
	defer util.CheckPanic()
	err := db.Client.WithContext(ctx).Debug().Table(RoomTable).
		Clauses(clause.OnConflict{
			Columns:   []clause.Column{{Name: "app_id"}, {Name: "room_id"}},
			UpdateAll: true,
		}).Create(&liveRoom).Error
	return err
}

func (repo *DbRoomRepo) GetRoomByRoomID(ctx context.Context, appID, roomID string) (*svc_entity.SvcRoom, error) {
	defer util.CheckPanic()
	var rs *svc_entity.SvcRoom
	err := db.Client.WithContext(ctx).Debug().Table(RoomTable).Where("app_id =? and room_id = ? and status <> ?", appID, roomID, RoomStatusFinish).First(&rs).Error
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			return nil, nil
		}
		return nil, err
	}
	return rs, nil
}

func (repo *DbRoomRepo) GetActiveRooms(ctx context.Context, appID string) ([]*svc_entity.SvcRoom, error) {
	defer util.CheckPanic()
	var rs []*svc_entity.SvcRoom
	err := db.Client.WithContext(ctx).Debug().Table(RoomTable).Where("app_id= ? and status = ?", appID, RoomStatusStart).Order("create_time desc").Limit(200).Find(&rs).Error
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			return nil, nil
		}
		return nil, err
	}
	return rs, nil
}

func (repo *DbRoomRepo) GetAllActiveRooms(ctx context.Context) ([]*svc_entity.SvcRoom, error) {
	defer util.CheckPanic()
	var rs []*svc_entity.SvcRoom
	err := db.Client.WithContext(ctx).Debug().Table(RoomTable).Where(" status = ?", RoomStatusStart).Order("create_time desc").Limit(200).Find(&rs).Error
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			return nil, nil
		}
		return nil, err
	}
	return rs, nil
}
