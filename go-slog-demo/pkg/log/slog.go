package log

import (
	"fmt"
	"golang.org/x/exp/slog"
	"os"
	"runtime"
	"strings"
)

type SlogLogger struct {
	logger    *slog.Logger
	level     *slog.Level
	addSource bool
}

type source struct {
	Function string `json:"function"`
	File     string `json:"file"`
	Line     int    `json:"line"`
}

var _ LoggerInf = &SlogLogger{}

func newSlogLogger(level Level, addSource bool) LoggerInf {
	slogLevel := level.GetSlogLevel()
	opts := &slog.HandlerOptions{AddSource: false, Level: slogLevel}
	return &SlogLogger{
		logger:    slog.New(slog.NewJSONHandler(os.Stdout, opts)),
		level:     &slogLevel,
		addSource: addSource,
	}
}

func (s *SlogLogger) Infow(msg string, keysAndValues ...any) {
	s.logger.Info(msg, s.AppendCaller(keysAndValues...)...)
}

func (s *SlogLogger) Warnw(msg string, keysAndValues ...any) {
	s.logger.Warn(msg, s.AppendCaller(keysAndValues...)...)
}

func (s *SlogLogger) Debugw(msg string, keysAndValues ...any) {
	s.logger.Debug(msg, s.AppendCaller(keysAndValues...)...)
}

func (s *SlogLogger) Fatalw(msg string, keysAndValues ...any) {
	s.logger.Error(msg, s.AppendCaller(keysAndValues...)...)
}

func (s *SlogLogger) Errorw(msg string, keysAndValues ...any) {
	s.logger.Error(msg, s.AppendCaller(keysAndValues...)...)
}

func (s *SlogLogger) ErrorStackW(error error, msg string, keysAndValues ...any) {
	s.logger.With("error", error).Error(msg, s.AppendCaller(keysAndValues...)...)
	printStack(4)
}

func (s *SlogLogger) Infof(format string, args ...any) {
	s.logger.Info(fmt.Sprintf(format, args...), s.AppendCaller()...)
}

func (s *SlogLogger) Warnf(format string, args ...any) {
	s.logger.Warn(fmt.Sprintf(format, args...), s.AppendCaller()...)
}

func (s *SlogLogger) Debugf(format string, args ...any) {
	s.logger.Debug(fmt.Sprintf(format, args...), s.AppendCaller()...)
}

func (s *SlogLogger) Fatalf(format string, args ...any) {
	s.logger.Error(fmt.Sprintf(format, args...), s.AppendCaller()...)
}

func (s *SlogLogger) Errorf(format string, args ...any) {
	s.logger.Error(fmt.Sprintf(format, args...), s.AppendCaller()...)
}

func (s *SlogLogger) ErrorStackF(error error, format string, args ...any) {
	s.logger.With("error", error).Error(fmt.Sprintf(format, args...), s.AppendCaller()...)
	printStack(4)

}

func (s *SlogLogger) WithName(name string) LoggerInf {
	s.logger = s.logger.With("logger", name)
	return s
}

func (s *SlogLogger) WithKeysAndValues(keysAndValues ...any) LoggerInf {
	s.logger = s.logger.With(keysAndValues...)
	return s
}

func (s *SlogLogger) AppendCaller(keyValues ...any) []any {
	s.addSource = false
	if !s.addSource {
		var pc uintptr
		var pcs [1]uintptr
		// skip [runtime.Callers, this function, this function's caller]
		runtime.Callers(4, pcs[:])
		pc = pcs[0]
		fs := runtime.CallersFrames([]uintptr{pc})
		f, _ := fs.Next()
		packageIndex := strings.LastIndex(f.Function, ".")
		fileIndex := strings.LastIndex(f.File, "/")
		file := f.Function[:packageIndex] + f.File[fileIndex:]
		keyValues = append(keyValues, "caller", fmt.Sprintf("%s:%d", file, f.Line))
		return keyValues
	}
	return keyValues
}
