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

import java.util.List;

/**
 * @author Karl Tauber
 */
public interface MarkdownSyntaxHighlighterAddon
{
	public interface Highlighter {
		void highlight(int begin, int end, String style);
	}

	List<String> getStylesheets();
	void highlight(String text, Highlighter highlighter);
}

// MarkdownSyntaxHighlighterAddon 是一个Java接口，它定义了一个用于Markdown编辑器的语法高亮插件。以下是该接口的详细中文解释：
//
//接口声明
//public interface MarkdownSyntaxHighlighterAddon
//这个接口名为 MarkdownSyntaxHighlighterAddon，它表示一个Markdown语法高亮插件。插件可以通过实现这个接口来为Markdown编辑器提供语法高亮功能。
//
//内部接口 Highlighter
//public interface Highlighter {
//	void highlight(int begin, int end, String style);
//}
//Highlighter 是一个内部接口，它定义了一个方法 highlight，用于在实际的文本范围内应用特定的样式。这个接口的参数包括：
//
//begin：要高亮的文本范围的起始索引。
//end：要高亮的文本范围的结束索引。
//style：应用于文本范围的高亮样式名称。
//方法 getStylesheets
//List<String> getStylesheets();
//这个方法返回一个包含样式表的路径或URL的列表。这些样式表定义了用于高亮Markdown语法的CSS样式。编辑器将使用这些样式表来渲染高亮文本。
//
//方法 highlight
//void highlight(String text, Highlighter highlighter);
//这个方法负责处理实际的语法高亮逻辑。它接收以下参数：
//
//text：要高亮的Markdown文本。
//highlighter：一个 Highlighter 对象，用于在实际的文本范围内应用样式。
//实现这个接口的类需要重写这个方法，并提供具体的语法高亮实现。在实现中，它会分析传入的文本，并使用 highlighter 对象来应用相应的样式。
//
//总结
//MarkdownSyntaxHighlighterAddon 接口定义了Markdown编辑器中语法高亮插件的基本结构。插件通过实现这个接口可以提供自定义的语法高亮功能，包括定义样式表和应用具体的样式到文本范围。这样，Markdown编辑器就可以支持不同的编程语言或特殊标记的语法高亮，从而提高用户体验。
