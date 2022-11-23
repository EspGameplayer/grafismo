import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IShirt } from '../shirt.model';

@Component({
  selector: 'jhi-shirt-detail',
  templateUrl: './shirt-detail.component.html',
})
export class ShirtDetailComponent implements OnInit {
  shirt: IShirt | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ shirt }) => {
      this.shirt = shirt;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
