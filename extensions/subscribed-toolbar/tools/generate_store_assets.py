#!/usr/bin/env python3
"""Generate Chrome Web Store marketing images without third-party libs."""
from __future__ import annotations

import struct
import zlib
from dataclasses import dataclass
from pathlib import Path
from typing import Dict, List, Sequence, Tuple

Color = Tuple[int, int, int]

FONT_HEIGHT = 7
FONT_WIDTH = 5
FONT: Dict[str, Sequence[str]] = {
    ' ': ['00000'] * FONT_HEIGHT,
    '-': ['00000', '00000', '00000', '11110', '00000', '00000', '00000'],
    '.': ['00000', '00000', '00000', '00000', '00000', '01100', '01100'],
    ':': ['00000', '01100', '01100', '00000', '01100', '01100', '00000'],
    '/': ['00001', '00010', '00100', '01000', '10000', '00000', '00000'],
    '?': ['11110', '00001', '00010', '00100', '00000', '00100', '00100'],
}

# Digits
FONT.update({
    '0': ['01110', '10001', '10011', '10101', '11001', '10001', '01110'],
    '1': ['00100', '01100', '00100', '00100', '00100', '00100', '01110'],
    '2': ['01110', '10001', '00001', '00010', '00100', '01000', '11111'],
    '3': ['11110', '00001', '00001', '01110', '00001', '00001', '11110'],
    '4': ['00010', '00110', '01010', '10010', '11111', '00010', '00010'],
    '5': ['11111', '10000', '11110', '00001', '00001', '10001', '01110'],
    '6': ['01110', '10000', '11110', '10001', '10001', '10001', '01110'],
    '7': ['11111', '00001', '00010', '00100', '01000', '01000', '01000'],
    '8': ['01110', '10001', '10001', '01110', '10001', '10001', '01110'],
    '9': ['01110', '10001', '10001', '01111', '00001', '00001', '01110'],
})

# Letters A-Z
FONT.update({
    'A': ['01110', '10001', '10001', '11111', '10001', '10001', '10001'],
    'B': ['11110', '10001', '10001', '11110', '10001', '10001', '11110'],
    'C': ['01110', '10001', '10000', '10000', '10000', '10001', '01110'],
    'D': ['11100', '10010', '10001', '10001', '10001', '10010', '11100'],
    'E': ['11111', '10000', '10000', '11110', '10000', '10000', '11111'],
    'F': ['11111', '10000', '10000', '11110', '10000', '10000', '10000'],
    'G': ['01110', '10001', '10000', '10111', '10001', '10001', '01111'],
    'H': ['10001', '10001', '10001', '11111', '10001', '10001', '10001'],
    'I': ['01110', '00100', '00100', '00100', '00100', '00100', '01110'],
    'J': ['00001', '00001', '00001', '00001', '10001', '10001', '01110'],
    'K': ['10001', '10010', '10100', '11000', '10100', '10010', '10001'],
    'L': ['10000', '10000', '10000', '10000', '10000', '10000', '11111'],
    'M': ['10001', '11011', '10101', '10101', '10001', '10001', '10001'],
    'N': ['10001', '11001', '10101', '10011', '10001', '10001', '10001'],
    'O': ['01110', '10001', '10001', '10001', '10001', '10001', '01110'],
    'P': ['11110', '10001', '10001', '11110', '10000', '10000', '10000'],
    'Q': ['01110', '10001', '10001', '10001', '10101', '10010', '01101'],
    'R': ['11110', '10001', '10001', '11110', '10100', '10010', '10001'],
    'S': ['01111', '10000', '10000', '01110', '00001', '00001', '11110'],
    'T': ['11111', '00100', '00100', '00100', '00100', '00100', '00100'],
    'U': ['10001', '10001', '10001', '10001', '10001', '10001', '01110'],
    'V': ['10001', '10001', '10001', '10001', '01010', '01010', '00100'],
    'W': ['10001', '10001', '10001', '10101', '10101', '11011', '10001'],
    'X': ['10001', '01010', '00100', '00100', '00100', '01010', '10001'],
    'Y': ['10001', '01010', '00100', '00100', '00100', '00100', '00100'],
    'Z': ['11111', '00001', '00010', '00100', '01000', '10000', '11111'],
})

@dataclass
class SimpleImage:
    width: int
    height: int
    bg: Color

    def __post_init__(self) -> None:
        self.rows: List[bytearray] = [bytearray(self.bg * self.width) for _ in range(self.height)]

    def set_pixel(self, x: int, y: int, color: Color) -> None:
        if 0 <= x < self.width and 0 <= y < self.height:
            row = self.rows[y]
            idx = x * 3
            row[idx:idx+3] = bytes(color)

    def fill_rect(self, x0: int, y0: int, x1: int, y1: int, color: Color) -> None:
        x0, y0 = max(0, x0), max(0, y0)
        x1, y1 = min(self.width, x1), min(self.height, y1)
        for y in range(y0, y1):
            row = self.rows[y]
            for x in range(x0, x1):
                idx = x * 3
                row[idx:idx+3] = bytes(color)

    def draw_border(self, x0: int, y0: int, x1: int, y1: int, color: Color, thickness: int = 1) -> None:
        for t in range(thickness):
            self.fill_rect(x0+t, y0+t, x1-t, y0+t+1, color)
            self.fill_rect(x0+t, y1-t-1, x1-t, y1-t, color)
            self.fill_rect(x0+t, y0+t, x0+t+1, y1-t, color)
            self.fill_rect(x1-t-1, y0+t, x1-t, y1-t, color)

    def vertical_gradient(self, top: Color, bottom: Color) -> None:
        for y in range(self.height):
            ratio = y / max(1, self.height - 1)
            color = tuple(int(top[i] * (1 - ratio) + bottom[i] * ratio) for i in range(3))
            row = self.rows[y]
            row[:] = bytes(color) * self.width

    def save(self, path: Path) -> None:
        raw = b''.join(b'\x00' + bytes(row) for row in self.rows)
        ihdr = struct.pack('>IIBBBBB', self.width, self.height, 8, 2, 0, 0, 0)
        data = _png_chunk(b'IHDR', ihdr) + _png_chunk(b'IDAT', zlib.compress(raw, 9)) + _png_chunk(b'IEND', b'')
        path.parent.mkdir(parents=True, exist_ok=True)
        with path.open('wb') as f:
            f.write(b'\x89PNG\r\n\x1a\n')
            f.write(data)


def _png_chunk(tag: bytes, data: bytes) -> bytes:
    length = struct.pack('>I', len(data))
    crc = struct.pack('>I', zlib.crc32(tag + data) & 0xFFFFFFFF)
    return length + tag + data + crc


def draw_char(img: SimpleImage, x: int, y: int, char: str, color: Color, scale: int = 3) -> int:
    pattern = FONT.get(char.upper(), FONT['?'])
    for row_idx, row in enumerate(pattern):
        for col_idx, val in enumerate(row):
            if val == '1':
                for dy in range(scale):
                    for dx in range(scale):
                        img.set_pixel(x + col_idx * scale + dx, y + row_idx * scale + dy, color)
    return (FONT_WIDTH * scale) + scale


def draw_text(img: SimpleImage, x: int, y: int, text: str, color: Color, scale: int = 3, line_spacing: int = 6) -> None:
    cursor_x, cursor_y = x, y
    for char in text:
        if char == '\n':
            cursor_y += FONT_HEIGHT * scale + line_spacing
            cursor_x = x
            continue
        cursor_x += draw_char(img, cursor_x, cursor_y, char, color, scale)


def draw_toolbar(img: SimpleImage, x: int, y: int, width: int, height: int) -> None:
    img.fill_rect(x, y, x + width, y + height, (248, 249, 251))
    img.draw_border(x, y, x + width, y + height, (203, 213, 225), 2)
    folder_w = width // 5
    folder_h = height - 30
    labels = ['DAILY READS', 'DEV TOOLS', 'SUBSCRIBED']
    for idx, label in enumerate(labels):
        fx = x + 20 + idx * (folder_w + 20)
        fy = y + 15
        img.fill_rect(fx, fy, fx + folder_w, fy + folder_h, (255, 255, 255))
        img.draw_border(fx, fy, fx + folder_w, fy + folder_h, (148, 163, 184), 1)
        draw_text(img, fx + 10, fy + 10, label, (15, 23, 42), scale=2)


def make_promo(size: Tuple[int, int], path: Path) -> None:
    img = SimpleImage(size[0], size[1], (0, 0, 0))
    img.vertical_gradient((15, 23, 42), (38, 99, 235))
    draw_text(img, 30, 30, 'SUBSCRIBED TOOLBAR', (255, 255, 255), scale=4)
    draw_text(img, 30, 140, 'SYNC BOOKMARKS FROM JSON FEEDS\nINTO A LIVE TOOLBAR FOLDER.', (220, 231, 241), scale=3)
    toolbar_width = size[0] - 120
    toolbar_height = 90
    draw_toolbar(img, 60, size[1] - toolbar_height - 60, toolbar_width, toolbar_height)
    img.save(path)


def make_options(path: Path) -> None:
    img = SimpleImage(1280, 800, (244, 247, 252))
    img.fill_rect(0, 0, 1280, 90, (15, 23, 42))
    draw_text(img, 40, 20, 'SUBSCRIBED TOOLBAR OPTIONS', (255, 255, 255), scale=3)
    img.fill_rect(0, 90, 280, 800, (226, 232, 240))
    draw_text(img, 40, 120, 'SETTINGS', (23, 37, 84), scale=3)
    sidebar_items = ['FEED URL', 'TARGET FOLDER', 'SYNC EVERY MIN', 'SYNC MODE', 'MANUAL SYNC']
    for idx, text in enumerate(sidebar_items):
        draw_text(img, 40, 180 + idx * 70, f'- {text}', (51, 65, 85), scale=2)
    form_x = 320
    field_width = 1280 - form_x - 60
    fields = [
        ('FEED URL', 'HTTPS://FEED.EXAMPLE/BOOKMARKS.JSON'),
        ('TARGET FOLDER NAME', 'TEAM RESOURCES'),
        ('SYNC EVERY (MINUTES)', '15'),
    ]
    for idx, (label, value) in enumerate(fields):
        top = 150 + idx * 160
        draw_text(img, form_x, top, label, (71, 85, 105), scale=2)
        box_top = top + 60
        img.fill_rect(form_x, box_top, form_x + field_width, box_top + 80, (255, 255, 255))
        img.draw_border(form_x, box_top, form_x + field_width, box_top + 80, (203, 213, 225), 2)
        draw_text(img, form_x + 20, box_top + 20, value, (15, 23, 42), scale=2)
    draw_text(img, form_x, 620, 'SYNC MODE', (71, 85, 105), scale=2)
    for idx, text in enumerate(['ADD-ONLY (PRESERVE EDITS)', 'FULL SYNC (REPLACE CONTENTS)']):
        top = 670 + idx * 70
        img.fill_rect(form_x, top, form_x + 30, top + 30, (255, 255, 255))
        img.draw_border(form_x, top, form_x + 30, top + 30, (148, 163, 184), 2)
        if idx == 0:
            img.fill_rect(form_x + 6, top + 6, form_x + 24, top + 24, (59, 130, 246))
        draw_text(img, form_x + 50, top - 5, text, (30, 41, 59), scale=2)
    # buttons
    buttons = [
        ('SAVE SETTINGS', (59, 130, 246), form_x),
        ('SYNC NOW', (16, 185, 129), form_x + 320),
    ]
    for label, color, x in buttons:
        top = 720
        img.fill_rect(x, top, x + 260, top + 70, color)
        draw_text(img, x + 30, top + 15, label, (255, 255, 255), scale=2)
    img.save(path)


def make_bookmarks(path: Path) -> None:
    img = SimpleImage(1280, 800, (243, 244, 246))
    img.fill_rect(0, 0, 1280, 130, (28, 45, 75))
    draw_text(img, 40, 30, 'BOOKMARKS BAR — STAY IN SYNC', (255, 255, 255), scale=3)
    img.fill_rect(0, 130, 1280, 170, (236, 239, 245))
    draw_text(img, 40, 190, 'SUBSCRIBED TOOLBAR MIRRORS FOLDERS AND LINKS FROM YOUR FEED.', (71, 85, 105), scale=2)
    draw_toolbar(img, 80, 240, 1120, 110)
    entries = [
        ('DAILY BRIEF', 'HTTPS://NEWS.EXAMPLE/BRIEF'),
        ('DESIGN REVIEW BOARD', 'HTTPS://MEET.EXAMPLE/DESIGN'),
        ('INCIDENT RUNBOOK', 'HTTPS://DOCS.EXAMPLE/RUNBOOK'),
        ('TEAM WIKI', 'HTTPS://WIKI.EXAMPLE/HOME'),
    ]
    top = 420
    for title, url in entries:
        img.fill_rect(80, top, 1200, top + 70, (255, 255, 255))
        img.draw_border(80, top, 1200, top + 70, (221, 226, 237), 1)
        draw_text(img, 100, top + 10, title, (30, 41, 59), scale=2)
        draw_text(img, 100, top + 35, url, (100, 116, 139), scale=2)
        top += 80
    img.save(path)


def main() -> None:
    out = Path('store-assets')
    make_promo((440, 280), out / 'promo-small.png')
    make_promo((920, 680), out / 'promo-large.png')
    make_options(out / 'screenshot-options.png')
    make_bookmarks(out / 'screenshot-bookmarks.png')


if __name__ == '__main__':
    main()
