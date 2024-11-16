/*
 * Copyright (c) 2018 Karl Tauber <karl at jformdesigner dot com>
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
public interface SpellCheckerAddon
{
	static public class Range
	{
		public final int start;
		public final int end;
		public final boolean blockBreak;

		public Range(int start, int end, boolean blockBreak) {
			this.start = start;
			this.end = end;
			this.blockBreak = blockBreak;
		}

		@Override
		public String toString() {
			return start + "-" + end + "  " + blockBreak;
		}
	}

	Range[] getAnnotatedRanges(String text);
}

// SpellCheckerAddon 是一个Java接口，它定义了一个用于Markdown编辑器的拼写检查插件。这个接口允许插件对文本进行拼写检查，并标注出可能的拼写错误。以下是该接口的详细解释：
//接口声明
//public interface SpellCheckerAddon
//这个接口名为 SpellCheckerAddon，它表示一个拼写检查插件，可以集成到Markdown编辑器中。
//内部类 Range
//接口内部定义了一个名为 Range 的静态公共类，用于表示文本中的范围，以及这个范围是否代表一个块级元素的断点。
//成员变量
//start：范围的起始位置（在文本中的索引）。
//end：范围的结束位置（在文本中的索引）。
//blockBreak：一个布尔值，表示这个范围是否代表一个块级元素的断点。
//构造函数
//public Range(int start, int end, boolean blockBreak)
//构造函数用于创建一个新的 Range 实例，需要提供范围的起始和结束位置，以及是否为块级元素断点的信息。
//toString 方法
//@Override
//public String toString() {
//	return start + "-" + end + "  " + blockBreak;
//}
//这个方法重写了 Object 类的 toString 方法，用于提供 Range 实例的字符串表示，方便调试和日志记录。
//方法 getAnnotatedRanges
//Range[] getAnnotatedRanges(String text);
//这个方法需要插件实现，它接受一个字符串参数 text，这是要进行检查的Markdown文本。该方法应该返回一个 Range 数组，每个 Range 对象代表文本中的一个范围，这个范围可能包含拼写错误。返回的数组中的每个 Range 对象都包含了拼写错误的确切位置以及是否为块级元素断点的信息。
//总结
//SpellCheckerAddon 接口为Markdown编辑器提供了一个扩展点，允许通过实现该接口的插件来提供拼写检查功能。插件通过 getAnnotatedRanges 方法返回文本中可能的拼写错误范围，编辑器可以使用这些信息来高亮显示错误或者提供修正建议。内部类 Range 用于封装关于错误范围的具体信息，包括位置和是否为块级元素断点。
