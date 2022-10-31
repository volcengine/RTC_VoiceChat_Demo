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
package general

import (
	"context"
	"github.com/go-redis/redis/v8"

	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/pkg/redis_cli"
)

const (
	keyGenerateRoomID = "rtc_demo:generate_room_id:"
)

func SetGeneratedRoomID(ctx context.Context, bizID string, roomID int64) {
	redis_cli.Client.Set(ctx, keyGenerateRoomID+bizID, roomID, 0)
}

func GetGeneratedRoomID(ctx context.Context, bizID string) (int64, error) {
	roomID, err := redis_cli.Client.Get(ctx, keyGenerateRoomID+bizID).Int64()
	if err == redis.Nil {
		return 0, nil
	}
	return roomID, err

}
