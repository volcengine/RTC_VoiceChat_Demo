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
package login_facade

import (
	"context"
	"time"

	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/application/login/login_entity"
	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/application/login/login_repository/login_implement"
)

var loginTokenRepository LoginTokenRepositoryInterface

func GetLoginTokenRepository() LoginTokenRepositoryInterface {
	if loginTokenRepository == nil {
		loginTokenRepository = &login_implement.LoginTokenRepositoryImpl{}
	}
	return loginTokenRepository
}

type LoginTokenRepositoryInterface interface {
	Save(ctx context.Context, token *login_entity.LoginToken) error

	ExistToken(ctx context.Context, token string) (bool, error)
	GetUserID(ctx context.Context, token string) string
	GetTokenCreatedAt(ctx context.Context, token string) (int64, error)
	SetTokenExpiration(ctx context.Context, token string, duration time.Duration)
	GetTokenExpiration(ctx context.Context, token string) time.Duration
}
