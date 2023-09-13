package log

import (
	"fmt"
	"go.uber.org/zap"
	"go.uber.org/zap/zapcore"
	"os"
)

type ZapLogger struct {
	logger    *zap.Logger
	zapConfig *zap.Config
}

var _ LoggerInf = &ZapLogger{}

func newZapLogger(level Level) LoggerInf {
	encoderConfig := zapcore.EncoderConfig{
		TimeKey:        "time",
		LevelKey:       "level",
		NameKey:        "logger",
		CallerKey:      "caller",
		MessageKey:     "msg",
		StacktraceKey:  "stacktrace",
		LineEnding:     zapcore.DefaultLineEnding,
		EncodeDuration: zapcore.SecondsDurationEncoder,
		EncodeTime:     zapcore.ISO8601TimeEncoder,
		EncodeLevel:    zapcore.LowercaseLevelEncoder,
		EncodeCaller:   zapcore.ShortCallerEncoder,
	}
	zapConfig := &zap.Config{
		Level:             zap.NewAtomicLevelAt(level.GetZapLevel()),
		Development:       true,
		DisableCaller:     false,
		DisableStacktrace: true,
		Sampling:          &zap.SamplingConfig{Initial: 100, Thereafter: 100},
		Encoding:          "json",
		EncoderConfig:     encoderConfig,
		OutputPaths:       []string{"stderr"},
		ErrorOutputPaths:  []string{"stderr"},
	}
	l, err := zapConfig.Build(zap.AddCallerSkip(1))
	if err != nil {
		_, _ = fmt.Fprintf(os.Stderr, "zap build logger failed err: %v", err)
		return nil
	}
	return &ZapLogger{
		logger:    l,
		zapConfig: zapConfig,
	}
}

func (z *ZapLogger) Infow(msg string, keysAndValues ...any) {
	z.logger.Sugar().Infow(msg, keysAndValues...)
}

func (z *ZapLogger) Warnw(msg string, keysAndValues ...any) {
	z.logger.Sugar().Warnw(msg, keysAndValues...)
}

func (z *ZapLogger) Debugw(msg string, keysAndValues ...any) {
	z.logger.Sugar().Debugw(msg, keysAndValues...)
}

func (z *ZapLogger) Fatalw(msg string, keysAndValues ...any) {
	z.logger.Sugar().Fatalw(msg, keysAndValues...)
}

func (z *ZapLogger) Errorw(msg string, keysAndValues ...any) {
	z.logger.Sugar().Errorw(msg, keysAndValues...)
}

func (z *ZapLogger) Infof(format string, args ...any) {
	z.logger.Sugar().Infof(format, args...)
}

func (z *ZapLogger) Warnf(format string, args ...any) {
	z.logger.Sugar().Warnf(format, args...)
}

func (z *ZapLogger) Debugf(format string, args ...any) {
	z.logger.Sugar().Debugf(format, args...)
}

func (z *ZapLogger) Fatalf(format string, args ...any) {
	z.logger.Sugar().Fatalf(format, args...)
}

func (z *ZapLogger) Errorf(format string, args ...any) {
	z.logger.Sugar().Errorf(format, args...)
}

func (z *ZapLogger) WithName(name string) {
	z.logger = z.logger.Named(name)
}

func (z *ZapLogger) WithKeysAndValues(keysAndValues ...any) {
	var fields []zap.Field
	for i := 0; i < len(keysAndValues); i += 2 {
		key := keysAndValues[i]
		var value any
		if len(keysAndValues) > i+1 {
			value = keysAndValues[i+1]
		}
		fields = append(fields, zap.Any(fmt.Sprint(key), value))
	}
	z.logger = z.logger.With(fields...)
}
