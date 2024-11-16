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

import javafx.scene.control.IndexRange;

import java.nio.file.Path;

/**
 * @author Karl Tauber
 */
public interface PreviewViewAddon
{
	javafx.scene.Node getNode();
	void update(String markdown, Path path);
	void scrollY(double value);
	void editorSelectionChanged(IndexRange range);
}

// PreviewViewAddon 是一个Java接口，它定义了一个用于Markdown编辑器的预览视图插件。这个接口允许插件提供一个自定义的节点来显示Markdown内容的预览，并且能够响应编辑器中的更改。以下是该接口的详细解释：
//接口声明
//public interface PreviewViewAddon
//这个接口名为 PreviewViewAddon，它表示一个用于Markdown预览视图的插件。通过实现这个接口，插件可以在Markdown编辑器中提供一个自定义的预览视图。
//方法 getNode
//javafx.scene.Node getNode();
//这个方法用于获取插件提供的自定义节点。这个节点将被添加到Markdown编辑器的预览区域中，用于显示Markdown内容的渲染结果。返回的节点可以是任何JavaFX Node 类型，例如 WebView 用于显示HTML内容。
//方法 update
//void update(String markdown, Path path);
//这个方法在编辑器中的Markdown内容发生变化时被调用。它接收以下参数：
//markdown：编辑器中的当前Markdown文本。
//path：当前编辑的Markdown文件的路径（如果有的话）。
//插件应该重写这个方法来更新预览视图，以反映编辑器中的最新内容。
//方法 scrollY
//void scrollY(double value);
//这个方法用于同步预览视图和编辑器的垂直滚动位置。当编辑器滚动时，这个方法会被调用，并传入一个代表滚动位置的 value。
//插件应该重写这个方法来调整预览视图的垂直滚动位置，以匹配编辑器的滚动位置。
//方法 editorSelectionChanged
//void editorSelectionChanged(IndexRange range);
//这个方法在编辑器中的文本选择发生变化时被调用。它接收以下参数：
//range：当前在编辑器中选择的文本范围。
//插件可以重写这个方法来响应编辑器中的文本选择变化，例如，高亮显示预览视图中的相应部分。
//总结
//PreviewViewAddon 接口允许插件为Markdown编辑器提供一个自定义的预览视图。插件可以实现这个接口来创建一个节点来显示Markdown内容的预览，并且能够响应编辑器中的内容更改、滚动位置变化和文本选择变化。这样的设计增加了Markdown编辑器的灵活性和功能性，允许开发者创建更加丰富和交互式的预览体验。
