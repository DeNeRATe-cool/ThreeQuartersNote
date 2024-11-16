/*
 * Copyright (c) 2017 Karl Tauber <karl at jformdesigner dot com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.threeQuarters.addons;

/**
 * @author Karl Tauber
 */
public interface SmartFormatAddon
{
	static char PROTECTED_SPACE = '\1';
	static char PROTECTED_TAB = '\2';

	static String LINE_BREAK = "\3";
	static String HARD_LINE_BREAK_SPACES = LINE_BREAK + "";
	static String HARD_LINE_BREAK_BACKSLASH = LINE_BREAK + "\\";
	static String SOFT_LINE_BREAK = LINE_BREAK + "s";

	static String SPECIAL_INDENT = "\4";

	String protect(String text);
	String unprotect(String text);
}

// SmartFormatAddon 是一个Java接口，它定义了一个用于Markdown编辑器的智能格式化插件。这个接口允许插件参与文本的格式化过程，特别是在处理空白字符和换行时。以下是该接口的详细解释：
//接口声明
//public interface SmartFormatAddon
//这个接口名为 SmartFormatAddon，它表示一个用于智能格式化Markdown文本的插件。
//静态常量
//接口中定义了一些静态常量，用于在格式化过程中保护特定的字符或表示特殊的格式：
//PROTECTED_SPACE：用于保护空格字符的转义字符。
//PROTECTED_TAB：用于保护制表符字符的转义字符。
//LINE_BREAK：用于表示换行的转义字符串。
//HARD_LINE_BREAK_SPACES：表示硬换行（由空格结束的行）的转义字符串。
//HARD_LINE_BREAK_BACKSLASH：表示硬换行（由反斜杠结束的行）的转义字符串。
//SOFT_LINE_BREAK：表示软换行的转义字符串。
//SPECIAL_INDENT：用于表示特殊缩进的转义字符串。
//方法 protect
//String protect(String text);
//这个方法用于保护文本中的特定字符，防止它们在格式化过程中被错误地处理。例如，它可以将空格和制表符替换为相应的转义字符，以确保它们在后续的处理中保持原样。
//方法 unprotect
//String unprotect(String text);
//这个方法用于将之前保护的字符恢复到它们原始的状态。在格式化过程完成后，这个方法将被调用来将转义字符替换回原来的空格、制表符等。
//总结
//SmartFormatAddon 接口允许插件在Markdown编辑器中自定义文本的格式化行为。通过实现这个接口，插件可以在格式化之前保护特定的字符，并在格式化之后恢复它们，确保文本的格式化既符合Markdown的规则，又不会破坏原有的文本结构。这样的设计使得Markdown编辑器能够支持更复杂的格式化需求，同时保持文本的可读性和可编辑性。
