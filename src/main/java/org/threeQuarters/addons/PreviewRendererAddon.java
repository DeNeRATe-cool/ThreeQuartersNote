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

import java.nio.file.Path;

/**
 * @author Karl Tauber
 */
public interface PreviewRendererAddon
{
	String preParse(String markdown, Path path);
	String postRender(String html, Path path);
}

// PreviewRendererAddon 是一个Java接口，它定义了一个用于Markdown编辑器的预览渲染插件。这个接口允许插件在Markdown文本转换为HTML之前和之后进行自定义处理。以下是该接口的详细解释：
//
//接口声明
//public interface PreviewRendererAddon
//这个接口名为 PreviewRendererAddon，它表示一个用于Markdown预览渲染的插件。通过实现这个接口，插件可以在Markdown文本被渲染为HTML之前或之后执行特定的操作。
//
//方法 preParse
//String preParse(String markdown, Path path);
//这个方法在Markdown文本被解析和转换为HTML之前被调用。它接收以下参数：
//
//markdown：要解析的原始Markdown文本。
//path：当前编辑的Markdown文件的路径（如果有的话）。
//该方法返回处理后的Markdown文本。插件可以实现这个方法来修改原始的Markdown文本，例如，插入额外的内容、处理特定的宏指令或者进行预处理。
//
//方法 postRender
//String postRender(String html, Path path);
//这个方法在Markdown文本已经被转换为HTML之后被调用。它接收以下参数：
//
//html：由Markdown文本转换得到的HTML内容。
//path：当前编辑的Markdown文件的路径（如果有的话）。
//该方法返回处理后的HTML文本。插件可以实现这个方法来修改生成的HTML，例如，添加自定义的脚本、样式或者进行后处理。
//
//总结
//PreviewRendererAddon 接口为Markdown编辑器提供了在Markdown文本解析和渲染过程中插入自定义逻辑的能力。通过实现这个接口，插件可以在Markdown文本被转换为HTML之前进行预处理，以及在转换之后进行后处理。这样的设计使得Markdown编辑器更加灵活和可扩展，能够支持各种自定义的渲染需求。
