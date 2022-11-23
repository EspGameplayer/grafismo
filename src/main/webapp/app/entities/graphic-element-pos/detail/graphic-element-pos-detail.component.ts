import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGraphicElementPos } from '../graphic-element-pos.model';

@Component({
  selector: 'jhi-graphic-element-pos-detail',
  templateUrl: './graphic-element-pos-detail.component.html',
})
export class GraphicElementPosDetailComponent implements OnInit {
  graphicElementPos: IGraphicElementPos | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ graphicElementPos }) => {
      this.graphicElementPos = graphicElementPos;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
