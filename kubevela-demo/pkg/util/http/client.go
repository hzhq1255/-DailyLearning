package http

import (
	"bytes"
	"encoding/json"
	"errors"
	"fmt"
	"io"
	"io/ioutil"
	"net/http"
	"reflect"
	"time"
)

type HTTPClient struct {
	Client *http.Client
}

func NewClient(timeout time.Duration) *HTTPClient {
	return &HTTPClient{
		Client: &http.Client{Timeout: timeout},
	}
}

func (c *HTTPClient) Do(method, url string, body any, headers map[string]string) (string, error) {
	var reqBody io.Reader
	switch reflect.TypeOf(body).Kind() {
	case reflect.String:
		reqBody = bytes.NewBufferString(body.(string))
	case reflect.TypeOf([]byte(nil)).Kind():
		reqBody = bytes.NewBuffer(body.([]byte))
	case reflect.Slice:
	case reflect.Struct:
		data, err := json.Marshal(body)
		if err != nil {
			return "", fmt.Errorf("marshal req %s body to json failed, err: %w", body, err)
		}
		reqBody = bytes.NewBuffer(data)
	default:
		reqBody = nil
	}

	req, err := http.NewRequest(method, url, reqBody)
	if err != nil {
		return "", err
	}

	for key, value := range headers {
		req.Header.Set(key, value)
	}

	resp, err := c.Client.Do(req)
	if err != nil {
		return "", err
	}
	defer resp.Body.Close()
	if resp.StatusCode < 200 || resp.StatusCode >= 300 {
		return "", errors.New(fmt.Sprintf("HTTP request failed with status code %d", resp.StatusCode))
	}

	responseBody, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		return "", err
	}
	return string(responseBody), nil
}
