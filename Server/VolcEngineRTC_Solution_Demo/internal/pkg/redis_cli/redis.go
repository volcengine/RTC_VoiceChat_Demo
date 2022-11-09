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
package redis_cli

import (
	"context"
	"time"

	"github.com/go-redis/redis/v8"
)

var (
	DialTimeout     = 500 * time.Millisecond
	ReadTimeout     = 500 * time.Millisecond
	WriteTimeout    = 500 * time.Millisecond
	PoolTimeout     = 500 * time.Millisecond
	IdleTimeout     = 60 * time.Minute
	MinRetryBackoff = 8 * time.Millisecond
	MaxRetryBackoff = 128 * time.Millisecond
)

var Client *redis.Client

func NewRedis(addr, password string) {
	Client = redis.NewClient(&redis.Options{
		Addr:            addr,
		Password:        password,
		MaxRetries:      3,
		MinRetryBackoff: MinRetryBackoff,
		MaxRetryBackoff: MaxRetryBackoff,
		PoolSize:        100,
		DialTimeout:     DialTimeout,
		ReadTimeout:     ReadTimeout,
		WriteTimeout:    WriteTimeout,
		PoolTimeout:     PoolTimeout,
		IdleTimeout:     IdleTimeout,
	})

	ctx := context.Background()
	_, err := Client.Ping(ctx).Result()
	if err != nil {
		panic("redis_cli init failed,error:" + err.Error())
	}

}
