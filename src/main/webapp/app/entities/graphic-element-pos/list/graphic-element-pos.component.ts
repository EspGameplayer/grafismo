import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IGraphicElementPos } from '../graphic-element-pos.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { GraphicElementPosService } from '../service/graphic-element-pos.service';
import { GraphicElementPosDeleteDialogComponent } from '../delete/graphic-element-pos-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-graphic-element-pos',
  templateUrl: './graphic-element-pos.component.html',
})
export class GraphicElementPosComponent implements OnInit {
  graphicElementPos: IGraphicElementPos[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected graphicElementPosService: GraphicElementPosService,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.graphicElementPos = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.isLoading = true;

    this.graphicElementPosService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<IGraphicElementPos[]>) => {
          this.isLoading = false;
          this.paginateGraphicElementPos(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.graphicElementPos = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IGraphicElementPos): number {
    return item.id!;
  }

  delete(graphicElementPos: IGraphicElementPos): void {
    const modalRef = this.modalService.open(GraphicElementPosDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.graphicElementPos = graphicElementPos;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateGraphicElementPos(data: IGraphicElementPos[] | null, headers: HttpHeaders): void {
    const linkHeader = headers.get('link');
    if (linkHeader) {
      this.links = this.parseLinks.parse(linkHeader);
    } else {
      this.links = {
        last: 0,
      };
    }
    if (data) {
      for (const d of data) {
        this.graphicElementPos.push(d);
      }
    }
  }
}
