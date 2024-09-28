### Tunneling VSCode Over X from Ubuntu to Windows: A Simple Solution

Recently, I was chatting with some peers about how resource-intensive development tasks can slow down our desktops. One person even joked about how their program was so demanding that it crashed Chrome. In response, I quipped, "I see the problem! There's a backslash in your path!" (a reference to the difference between Windows’ backslash paths and the forward slash used by Linux and Unix-based systems).

### Dev Environments and Horsepower

For years, I’ve coded on Linux, and I strongly believe that having a dedicated development machine is one of the best things you can do for performance. Windows has definitely come a long way since the days of version 3.1—it can be surprisingly performant when properly tuned. But no machine is going to be at its best while trying to handle everything at once: browsing the web, running games, editing documents, and handling development tasks simultaneously.

This became especially relevant as I started working from home. I found myself needing my Windows machine for quick breaks to play "The Long Dark," and didn't want development tasks slowing down my gaming sessions.

### My Solution: A Dedicated Linux Dev Box

I repurposed an old PC, installed Ubuntu on it, and now I use it as my dedicated dev machine. The beauty of this setup is that my Linux box is free from the distractions and overhead of running non-development-related tasks, making it more powerful in practice than my Windows machine. Plus, by using SSH to connect to the Linux machine (I’ll call it "Dubuntu" for fun), I can tunnel X and run graphical applications on my Windows desktop while keeping all the heavy lifting on Dubuntu.

If you’ve got an old PC lying around, you can easily set this up yourself!

### Step 1: Install Ubuntu on Your Old PC

First, you’ll need to install Ubuntu on your old machine. Here’s a quick guide to get you started:

- [How to create a live USB with Ubuntu](https://ubuntu.com/tutorials/create-a-usb-stick-on-windows)
- [How to boot from USB and install Ubuntu](https://ubuntu.com/tutorials/install-ubuntu-desktop)

Once you've got Ubuntu up and running, connect the machine to your network so it can access the web. After that, install [VSCode](https://code.visualstudio.com/) on the machine by following these [simple steps](https://code.visualstudio.com/docs/setup/linux).

### Step 2: Setting Up SSH and X Forwarding

Now that you’ve got your Ubuntu machine (Dubuntu) set up as your dedicated development box, you can connect to it remotely from your Windows machine. Here's how to set up X forwarding and run VSCode remotely:

#### 1. Install VcXsrv on Windows

VcXsrv is an X server for Windows that lets you display graphical applications running on a remote Linux machine. You can download it from [here](https://sourceforge.net/projects/vcxsrv/). Be sure to install it with a desktop icon—you’ll need it to start the X server each time.

#### 2. Install PuTTY

If you haven’t already, install PuTTY, a popular SSH client for Windows. Download it [here](https://www.chiark.greenend.org.uk/~sgtatham/putty/latest.html).

#### 3. Start VcXsrv

Once VcXsrv is installed, launch it using the desktop icon. This will start the X server on your Windows machine, which will allow you to display remote Linux apps.

#### 4. Enable X Forwarding in PuTTY

When you connect to your Ubuntu machine using PuTTY, make sure X forwarding is enabled:

- Open PuTTY and navigate to **Connection** → **SSH** → **X11**.
- Check the box for **Enable X11 forwarding**.
- Set the **X display location** to `localhost:0.0`.

Now, when you SSH into Dubuntu from PuTTY, graphical applications can be forwarded and displayed on your Windows machine.

### Step 3: Launch VSCode from Ubuntu on Windows

Once you're connected to Dubuntu through PuTTY with X forwarding enabled, it’s time to launch VSCode. At the command line in PuTTY, type:

```
code
```

If everything is set up correctly, a VSCode window served from your Ubuntu machine should pop up on your Windows desktop! Now, you’ve got a powerful, dedicated dev machine working in the background, while keeping your Windows machine free for other tasks like gaming or general productivity.

### Bonus: Set Up a Remote Database

While you're at it, you can install [MariaDB](https://mariadb.org/) on Dubuntu, turning it into a remote database server. This is great for testing or hosting your local projects. Plus, managing a home network and setting up remote services teaches you a lot about networking protocols like TCP, which can be an invaluable skill for developers.

### Wrapping It Up

This setup has been a game changer for me. By using an old PC as a dedicated dev machine running Ubuntu, I can free up my Windows machine for non-development tasks and enjoy better performance all around. So, if you've got an old PC gathering dust, consider turning it into your very own dedicated development server!

### Potential Issues

Your vanilla Ubuntu setup should allow xforwarding, but if it doesn't, try asking chatGPT how to enable xForwarding on your ubuntu box

**Helpful Links:**
- [Create a live USB with Ubuntu](https://ubuntu.com/tutorials/create-a-usb-stick-on-windows)
- [How to install Ubuntu](https://ubuntu.com/tutorials/install-ubuntu-desktop)
- [Download VSCode](https://code.visualstudio.com/)
- [Download VcXsrv](https://sourceforge.net/projects/vcxsrv/)
- [Download PuTTY](https://www.chiark.greenend.org.uk/~sgtatham/putty/latest.html)
- [MariaDB Official Site](https://mariadb.org/)

By setting this up, you’ll not only streamline your development environment but also gain some hands-on experience with networking and system administration—skills that will definitely come in handy!

### How to Edit This Blog Post

1. Click [here](https://github.com/fishdan/Utilities/edit/main/blog/tunneling-vscode-from-ubuntu.md) to edit this blog post.
2. Make your changes in the editor.
3. Write a descriptive commit message.
4. Submit a Pull Request with your changes.

