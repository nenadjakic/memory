package com.github.nenadjakic.memory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.command.annotation.CommandScan;
import org.springframework.shell.style.FigureSettings;
import org.springframework.shell.style.StyleSettings;
import org.springframework.shell.style.Theme;
import org.springframework.shell.style.ThemeSettings;

@Configuration
@CommandScan
public class MemoryGameConfiguration {
    @Bean
    public Theme customTheme() {
        return new Theme() {
            @Override
            public String getName() {
                return "custom";
            }
            @Override
            public ThemeSettings getSettings() {
                return new CustomThemeSettings();
            }
        };
    }

    static class CustomThemeSettings extends ThemeSettings {
        CustomThemeSettings() {
            super(new CustomStyleSettings(), FigureSettings.defaults());
        }
    }

    static class CustomStyleSettings extends StyleSettings {

        @Override
        public String highlight() {
            return "bold,italic,fg:bright-cyan";
        }

        @Override
        public String background() {
            return "bg:gray";
        }

        @Override
        public String dialogBackground() {
            return "bg:green";
        }

        @Override
        public String buttonBackground() {
            return "bg:red";
        }

        @Override
        public String menubarBackground() {
            return "bg:magenta";
        }

        @Override
        public String statusbarBackground() {
            return "bg:cyan";
        }
    }
}
