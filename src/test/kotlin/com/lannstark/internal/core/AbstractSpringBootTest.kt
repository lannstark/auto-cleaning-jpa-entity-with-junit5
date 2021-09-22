package com.lannstark.internal.core

import com.lannstark.CleaningSpringBootTest
import com.lannstark.internal.WebApplication
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [WebApplication::class])
abstract class AbstractSpringBootTest : CleaningSpringBootTest()