/*
 * Project senpai, 2023-02-14T16:36+0100
 *
 * Copyright 2023-2023 Alessandro 'kLeZ' Accardo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.klez.senpai.view.dialog

import com.intellij.uiDesigner.core.AbstractLayout
import com.intellij.util.ui.GridBag
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JPanel

class Panel(nbColumnsGrid: Int = 1) : JPanel(GridBagLayout()) {
	val gridBag: GridBag = GridBag()
		.setDefaultInsets(0, nbColumnsGrid, AbstractLayout.DEFAULT_VGAP, AbstractLayout.DEFAULT_HGAP)
		.setDefaultWeightX(1.0)
		.setDefaultFill(GridBagConstraints.HORIZONTAL)

	constructor(minWight: Int, minHeight: Int, nbColumnsGrid: Int) : this(nbColumnsGrid) {
		preferredSize = Dimension(minWight, minHeight)
		minimumSize = Dimension(minWight, minHeight)
	}
}
