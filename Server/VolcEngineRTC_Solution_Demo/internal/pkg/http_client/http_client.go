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
package http_client

import (
	"github.com/valyala/fasthttp"
)

type HeaderPair struct {
	Key   string
	Value string
}

func NewHeaderPair(key, value string) HeaderPair {
	return HeaderPair{
		Key:   key,
		Value: value,
	}
}

var client = &fasthttp.Client{}

func DoRequest(url, method string, body []byte, headers ...HeaderPair) (int, []byte, error) {
	req := fasthttp.AcquireRequest()
	resp := fasthttp.AcquireResponse()

	defer func() {
		fasthttp.ReleaseResponse(resp)
		fasthttp.ReleaseRequest(req)
	}()

	req.SetRequestURI(url)
	req.SetBody(body)
	req.Header.SetMethod(method)
	req.Header.Set("Content-Type", "application/json")

	for _, header := range headers {
		req.Header.Set(header.Key, header.Value)
	}

	if err := client.Do(req, resp); err != nil {
		return 0, nil, err
	}

	return resp.StatusCode(), resp.Body(), nil
}
